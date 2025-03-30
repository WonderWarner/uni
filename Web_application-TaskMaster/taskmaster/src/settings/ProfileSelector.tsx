import './ProfileSelector.less';
import { ProfileIcon } from '../common/ProfileIcon';

/**
 * Props for the ProfileSelector component.
 */
interface ProfileSelectorProps {
    profileOptions: string[];
    selectedProfile: string;
    onChange: (selected: string) => void;
}

/**
 * Renders a profile selector with multiple profile options.
 * @param profileOptions - The available profile options.
 * @param selectedProfile - The currently selected profile.
 * @param onChange - Function to call when the selected profile changes.
 */
export function ProfileSelector({
    profileOptions,
    selectedProfile,
    onChange,
}: Readonly<ProfileSelectorProps>) {
    return (
        <div class="profile-selector">
            <h3>Select Profile Icon</h3>
            <div class="profile-options">
                {profileOptions.map((icon) => (
                    <label key={icon} class="profile-option">
                        <input
                            type="radio"
                            name="profile"
                            value={icon}
                            checked={selectedProfile === icon}
                            onChange={() => onChange(icon)}
                        />
                        <ProfileIcon icon={icon} name={icon} />
                    </label>
                ))}
            </div>
        </div>
    );
}
