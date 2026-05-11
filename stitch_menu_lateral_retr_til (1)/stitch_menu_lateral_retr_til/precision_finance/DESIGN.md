---
name: Precision Finance
colors:
  surface: '#f9f9ff'
  surface-dim: '#d3daea'
  surface-bright: '#f9f9ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f0f3ff'
  surface-container: '#e7eefe'
  surface-container-high: '#e2e8f8'
  surface-container-highest: '#dce2f3'
  on-surface: '#151c27'
  on-surface-variant: '#444748'
  inverse-surface: '#2a313d'
  inverse-on-surface: '#ebf1ff'
  outline: '#747878'
  outline-variant: '#c4c7c7'
  surface-tint: '#5f5e5e'
  primary: '#000000'
  on-primary: '#ffffff'
  primary-container: '#1c1b1b'
  on-primary-container: '#858383'
  inverse-primary: '#c8c6c5'
  secondary: '#006c49'
  on-secondary: '#ffffff'
  secondary-container: '#6cf8bb'
  on-secondary-container: '#00714d'
  tertiary: '#000000'
  on-tertiary: '#ffffff'
  tertiary-container: '#410004'
  on-tertiary-container: '#ef4444'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#e5e2e1'
  primary-fixed-dim: '#c8c6c5'
  on-primary-fixed: '#1c1b1b'
  on-primary-fixed-variant: '#474646'
  secondary-fixed: '#6ffbbe'
  secondary-fixed-dim: '#4edea3'
  on-secondary-fixed: '#002113'
  on-secondary-fixed-variant: '#005236'
  tertiary-fixed: '#ffdad7'
  tertiary-fixed-dim: '#ffb3ad'
  on-tertiary-fixed: '#410004'
  on-tertiary-fixed-variant: '#930013'
  background: '#f9f9ff'
  on-background: '#151c27'
  surface-variant: '#dce2f3'
typography:
  headline-xl:
    fontFamily: Manrope
    fontSize: 40px
    fontWeight: '700'
    lineHeight: 48px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Manrope
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 40px
    letterSpacing: -0.02em
  headline-md:
    fontFamily: Manrope
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
    letterSpacing: -0.01em
  body-lg:
    fontFamily: Manrope
    fontSize: 18px
    fontWeight: '400'
    lineHeight: 28px
  body-md:
    fontFamily: Manrope
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  label-md:
    fontFamily: Manrope
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
    letterSpacing: 0.05em
  label-sm:
    fontFamily: Manrope
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
rounded:
  sm: 0.5rem
  DEFAULT: 1rem
  md: 1.5rem
  lg: 2rem
  xl: 3rem
  full: 9999px
spacing:
  base: 8px
  xs: 4px
  sm: 12px
  md: 24px
  lg: 40px
  xl: 64px
  gutter: 24px
  margin: 32px
---

## Brand & Style

The design system is anchored in the principles of financial clarity, discipline, and modern professionalism. It seeks to evoke a sense of absolute control and calm, transforming the often-stressful task of money management into a streamlined, premium experience. 

The aesthetic follows a **Corporate / Modern** direction with a heavy emphasis on **Minimalism**. By utilizing a high-contrast primary palette against expansive off-white surfaces, the design system ensures that data remains the hero. Visual noise is aggressively eliminated to foster trust, positioning the product as a reliable tool for serious wealth management.

## Colors

The color palette is strictly functional. The primary color is a deep, near-black ink, used for high-emphasis actions and core branding elements to convey stability and authority. 

The background utilizes an off-white "Zinc" tint to reduce eye strain compared to pure white, providing a sophisticated canvas for data visualization. Semantic accents are used sparingly but decisively:
- **Success (Green):** Represents income, positive balances, and completed goals.
- **Error (Red):** Reserved for expenses, over-budget warnings, and critical alerts.
- **Neutrals:** A range of slate greys handles secondary text, borders, and inactive states to maintain a clean hierarchy.

## Typography

This design system uses **Manrope** as the sole typeface. Chosen for its modern, geometric construction and exceptional legibility in data-heavy environments, it bridges the gap between approachable and institutional.

Headlines use tighter letter spacing and heavier weights to create a sense of importance and structure. Body text is optimized for readability with generous line heights. Labels often utilize a semi-bold weight and subtle tracking to differentiate metadata from primary content.

## Layout & Spacing

The design system employs a **Fixed Grid** philosophy for desktop layouts and a fluid model for mobile. A 12-column grid system ensures structural alignment across complex financial dashboards.

The spacing rhythm is built on an **8px linear scale**. This consistency ensures that every element, from the padding inside a "Pill" button to the margin between two cards, feels intentional and mathematically balanced. Use `md` (24px) as the standard container padding to ensure a breathable, premium feel.

## Elevation & Depth

To maintain a "clean and modern" aesthetic, this design system avoids heavy shadows. Depth is communicated through **Tonal Layers** and **Ambient Shadows**:

1.  **Level 0 (Base):** The off-white background (`#F9FAFB`).
2.  **Level 1 (Cards):** Pure white surfaces (`#FFFFFF`) with a very soft, diffused shadow (0px 4px 20px rgba(0,0,0,0.04)).
3.  **Level 2 (Overlays/Modals):** Pure white surfaces with a more pronounced shadow (0px 10px 30px rgba(0,0,0,0.08)) to indicate interaction priority.

Borders are used sparingly, primarily as low-contrast dividers within white cards to separate list items.

## Shapes

The design system utilizes a **Pill-shaped** (Full Rounded) philosophy for interactive elements to provide a friendly, modern contrast to the structured, data-heavy grid.

- **Action Elements:** All buttons and chips must use the maximum border-radius (pill-shaped) to clearly distinguish them from content containers.
- **Containers:** Cards and input fields use a refined `rounded-lg` (1rem) corner radius to soften the layout while maintaining a professional look.
- **Data Visuals:** Progress bars and chart nodes follow the pill-shape convention for consistency.

## Components

### Buttons
Primary buttons are pill-shaped, using the deep black primary color with white text. Hover states should transition to a very dark grey. Secondary buttons use a subtle light grey fill with primary-colored text.

### Modern Cards
Cards are the primary container for financial data. They feature a white background, subtle ambient shadows, and `1rem` rounded corners. Header sections within cards should be clearly defined by typography rather than heavy borders.

### Clean Tables
Tables are designed for high-density information. They use no vertical borders; only horizontal separators in a very light grey (`#F3F4F6`). Header rows use the `label-md` typographic style for clear categorization.

### Input Fields
Inputs are outlined with a light grey border that transitions to primary black on focus. They should match the height of buttons for visual alignment and use the `1rem` roundedness for a soft, modern feel.

### Financial Chips
Used for transaction categories (e.g., "Food", "Rent"). These are small, pill-shaped badges with a low-opacity tint of the category color and high-contrast text.

### Data Visualizations
Charts should utilize a limited palette of the primary, success, and error colors. Lines and bars should have rounded caps to align with the overall shape language of the design system.