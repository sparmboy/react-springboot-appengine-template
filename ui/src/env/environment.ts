import productionConfig from "./production.json";
import developmentConfig from "./development.json";

export type Environment = {
    host: string
}

export const environmentConfig:Environment = process.env.NODE_ENV === 'production' ? productionConfig : developmentConfig;