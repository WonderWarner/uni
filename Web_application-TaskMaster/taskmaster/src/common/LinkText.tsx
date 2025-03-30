import "./LinkText.less";

/**
 * Props for the LinkText component.
 */
export type LinkTextProps = {
    text: string;
    onClick: () => void;
}

/**
 * Renders a clickable text element.
 * @param text - The text to display.
 * @param onClick - Function to call when the text is clicked.
 */
export function LinkText({ text, onClick }: Readonly<LinkTextProps>) {
    return <p class="LinkText" onClick={onClick}>{text}</p>;
}