import './ImageButton.less';

/**
 * Props for the ImageButton component.
 */
interface ImageButtonProps {
    imageSrc: string;
    label: string;
    onClick: () => void;
}

/**
 * Renders a button with an image and label.
 * @param imageSrc - The source URL of the image to display.
 * @param label - The label to display next to the image.
 * @param onClick - Function to call when the button is clicked.
 */
export function ImageButton({ imageSrc, label, onClick }: Readonly<ImageButtonProps>) {
    return (
        <div class="image-button" onClick={onClick}>
            <img src={imageSrc} alt={label} />
            <span>{label}</span>
        </div>
    );
}