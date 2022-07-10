import { defineConfig } from 'cypress';

export default defineConfig({
    video: false,
    screenshotOnRunFailure: false,
    env: {
        resourceServiceUrl: 'http://localhost:5001/api',
        songServiceUrl: 'http://localhost:5002/api',
    },
    e2e: {
        setupNodeEvents(on, config) {
            // implement node event listeners here
        },
    },
});
