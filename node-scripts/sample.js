import ModelClient, { isUnexpected } from "@azure-rest/ai-inference";
import { AzureKeyCredential } from "@azure/core-auth";
import 'dotenv/config';
import fs from 'fs';
import { promisify } from 'util';

const readFile = promisify(fs.readFile);

// Configuración ampliada
const token = process.env.GITHUB_TOKEN;
const endpoint = "https://models.github.ai/inference";

// Modelos disponibles (ahora con soporte multimodal)
const MODELS = {
    OPENAI: {
        id: "openai/gpt-4.1",
        params: { temperature: 1.0, top_p: 1.0 },
        multimodal: false
    },
    DEEPSEEK: {
        id: "deepseek/DeepSeek-V3-0324",
        params: { temperature: 1.0, top_p: 1.0, max_tokens: 1000 },
        multimodal: false
    },
    META: {
        id: "meta/Llama-4-Scout-17B-16E-Instruct",
        params: { temperature: 1.0, top_p: 1.0, max_tokens: 1000 },
        multimodal: false
    },
    GPT4V: {
        id: "openai/gpt-4-vision-preview",
        params: { temperature: 0.7, max_tokens: 2000 },
        multimodal: true
    }
};

class AIManager {
    constructor() {
        this.client = ModelClient(
            endpoint,
            new AzureKeyCredential(token)
        );
    }

    async #encodeImageToBase64(imagePath) {
        try {
            const imageBuffer = await readFile(imagePath);
            return imageBuffer.toString('base64');
        } catch (error) {
            throw new Error(`Error procesando imagen: ${error.message}`);
        }
    }

    async query(modelKey, prompt, options = {}) {
        try {
            const {
                systemMessage = "You are a helpful assistant.",
                imagePath = null
            } = options;

            const modelConfig = MODELS[modelKey];
            if (!modelConfig) throw new Error(`Modelo ${modelKey} no existe`);

            // Preparar mensajes según el tipo de modelo
            const messages = [
                { role: "system", content: systemMessage }
            ];

            if (modelConfig.multimodal && imagePath) {
                const base64Image = await this.#encodeImageToBase64(imagePath);
                messages.push({
                    role: "user",
                    content: [
                        { type: "text", text: prompt },
                        {
                            type: "image_url",
                            image_url: {
                                url: `data:image/jpeg;base64,${base64Image}`
                            }
                        }
                    ]
                });
            } else {
                messages.push({ role: "user", content: prompt });
            }

            const response = await this.client.path("/chat/completions").post({
                body: {
                    messages,
                    model: modelConfig.id,
                    ...modelConfig.params
                }
            });

            if (isUnexpected(response)) {
                throw new Error(response.body.error?.message || "Error desconocido");
            }

            return {
                model: modelKey,
                response: response.body.choices[0].message.content,
                isMultimodal: modelConfig.multimodal && !!imagePath
            };
        } catch (error) {
            console.error(`Error con ${modelKey}:`, error.message);
            return { model: modelKey, error: error.message };
        }
    }

}

(async () => {
    const aiManager = new AIManager();

    // Consulta con imagen (GPT-4V)
    const imagePath = './ruta/a/tu/imagen.jpg'; // Reemplaza con tu ruta
    const multimodalResponse = await aiManager.query(
        "GPT4V",
        "¿Qué hay en esta imagen?",
        {
            systemMessage: "Eres un asistente visual experto",
            imagePath
        }
    );

    console.log("Análisis multimodal:", multimodalResponse);

    // Consulta estándar de texto (comparación)
    const textResponses = await aiManager.compareModels(
        "Describe los principios de la física cuántica",
        { systemMessage: "Eres un profesor de física" }
    );

    console.log("\nComparación de modelos de texto:");
    console.log(textResponses);
})();