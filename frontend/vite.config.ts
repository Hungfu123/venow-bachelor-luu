import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from 'tailwindcss'
import svgr from "@svgr/rollup";

export default defineConfig({
  plugins: [
    react(),
    svgr({
      include: "**/*.svg",
    })
  ],
  css: {
    postcss: {
      plugins: [
        tailwindcss() // TailwindCSS-Plugin
      ],
    },
  },
});