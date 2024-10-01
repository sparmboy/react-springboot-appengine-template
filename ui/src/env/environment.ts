import productionConfig from "./production.json";
import developmentConfig from "./development.json";

export type Environment = {
    host: string
}


console.log("Loaded dev config=",developmentConfig)

export const environmentConfig:Environment = process.env.NODE_ENV === 'production' ? productionConfig : developmentConfig;

console.log("Enviornment config=",environmentConfig)
