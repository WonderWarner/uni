import './IconButton.less';
//Ehhez kellett a parancs: npm install @fortawesome/react-fontawesome @fortawesome/free-solid-svg-icons @fortawesome/free-regular-svg-icons @fortawesome/fontawesome-svg-core
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';

/**
 * Props for the IconButton component.
 */
interface IconButtonProps {
    icon: IconDefinition;
    label: string;
    onClick: () => void;
    disabled?: boolean;
}

/**
 * Renders a button with an icon and label.
 * @param icon - The FontAwesome icon to display.
 * @param label - The label to display next to the icon.
 * @param onClick - Function to call when the button is clicked.
 * @param disabled - Whether the button is disabled.
 */
export function IconButton({ icon, label, onClick, disabled }: Readonly<IconButtonProps>) {
    return (
        <button class="icon-button" onClick={onClick} disabled={disabled}>
            <FontAwesomeIcon icon={icon} className="icon" />
            <span>{label}</span>
        </button>
    );
}
