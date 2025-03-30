import { useState } from 'preact/hooks';
import './ProfileIcon.less';

/**
 * Props for the ProfileIcon component.
 */
interface ProfileIconProps {
    name: string;
    icon: string;
}

/**
 * Renders a profile icon with a tooltip.
 * @param name - The name to display in the tooltip.
 * @param icon - The icon image filename (without extension).
 */
export function ProfileIcon({ name, icon }: Readonly<ProfileIconProps>) {
    const [isHovered, setIsHovered] = useState(false);

    return (
        <div
            class="profile-icon"
            onMouseEnter={() => setIsHovered(true)}
            onMouseLeave={() => setIsHovered(false)}
        >
            <img src={`/res/${icon}.png`} alt={name} />
            {isHovered && <div class="tooltip">{name}</div>}
        </div>
    );
}
