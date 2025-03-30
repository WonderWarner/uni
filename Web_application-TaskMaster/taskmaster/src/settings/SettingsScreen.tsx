import { useState, useEffect} from 'preact/hooks';
import { route } from 'preact-router';
import { ProfileIcon } from '../common/ProfileIcon';
import { IconButton } from '../common/IconButton';
import { LabeledInput } from '../common/LabeledInput';
import './SettingsScreen.less';
import { faArrowLeft } from '@fortawesome/free-solid-svg-icons';
import { taskService, OutgoingPacket } from '../TaskService';
import { ProfileSelector } from './ProfileSelector';

/**
 * Renders the settings screen.
 */
export function SettingsScreen() {
    const [user, setUser] = useState(taskService.storage.user);
    const [newEmail, setNewEmail] = useState(user.email);
    const [selectedProfile, setSelectedProfile] = useState(user.picture);

    /**
     * Fetches the user data from the task service and updates the state.
     */
    const fetchUser = () => {
        setUser(taskService.storage.user);
        setNewEmail(taskService.storage.user.email);
        setSelectedProfile(taskService.storage.user.picture);
    }

    useEffect(() => {
        taskService.addListener(fetchUser); // Hallgató eseményekre
        fetchUser(); // Azonnali frissítés
        return () => taskService.removeListener(fetchUser); // Tisztítás
    }, []);

    /**
     * Handles the save action for the user settings.
     */
    const handleSave = () => {
        const packet: OutgoingPacket = {
            type: 'edit_user',
            id: user.id,
            name: user.name,
            email: newEmail,
            picture: selectedProfile,
            theme: user.theme,
        };
        taskService.send(packet);
    };

    const profileOptions = [
        'default',
        'dog',
        'cat',
        'panda',
        'bear',
    ];

    return (
        <div class="settings-screen">
            <div class="settings-content">
                <div class="settings-left">
                    <IconButton
                        icon={faArrowLeft}
                        label="Back"
                        onClick={() => route('/')}
                    />
                    <h1>Settings</h1>
                    <ProfileIcon icon={user.picture} name="" />
                    <div class="user-info">
                        <p><strong>Name:</strong> {user.name}</p>
                        <p><strong>Email:</strong> {user.email}</p>
                    </div>
                    <LabeledInput
                        label="New Email"
                        type="email"
                        placeholder='Enter new email'
                        value={newEmail}
                        onChange={setNewEmail}
                    />
                    <ProfileSelector
                        profileOptions={profileOptions}
                        selectedProfile={selectedProfile}
                        onChange={setSelectedProfile}
                    />
                    <button class="save-button" onClick={handleSave}>Save</button>
                </div>
                <div class="settings-right">
                    <img src="/res/character.png" alt="Character" class="character-image" />
                </div>
            </div>
        </div>
    );
}
