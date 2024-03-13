/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        'primary': { //회색
          '50': '#f5f5f6',
          '100': '#e6e6e7',
          '200': '#d0d0d1',
          '300': '#b0b0b0',
          '400': '#888888',
          '500': '#79797a',
          '600': '#5d5d5d',
          '700': '#4f4f4f',
          '800': '#454545',
          '900': '#3c3c3d',
          '950': '#262626',
        },
      
      'secondary': {//아쿠아
          '50': '#edfffe',
          '100': '#c0feff',
          '200': '#81fbff',
          '300': '#3af8ff',
          '400': '#00ffff',
          '500': '#00e1e2',
          '600': '#00b2b7',
          '700': '#008c91',
          '800': '#006c72',
          '900': '#04585d',
          '950': '#00343a',
        },
      },
      fontFamily:{
        'Pretendard':['Pretendard-Regular']
      },
    },
  },
  plugins: ["prettier-plugin-tailwindcss"],
}

