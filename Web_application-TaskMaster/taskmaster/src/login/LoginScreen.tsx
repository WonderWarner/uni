import { useState } from 'preact/hooks';
import './LoginScreen.less';
import { LabeledInput } from '../common/LabeledInput';
import { LinkText } from '../common/LinkText';
import { taskService } from '../TaskService';

/**
 * Renders the login screen.
 */
export function LoginScreen() {
    let [isRegister, setRegister] = useState(false);
    let [username, setUsername] = useState("");
    let [email, setEmail] = useState("");
    let [password, setPassword] = useState("");

    /**
     * Handles the login or registration action.
     */
    function loginRegister() {
        if (isRegister) {
            taskService.send({ type: "register", username, email, password });
            setRegister(false);
        } else {
            taskService.send({ type: "login", username, password });
        }
    }

    return <div class="LoginScreen">
        <div class="LoginContent">
            <img src="/res/title_logo.png" alt="TaskMaster Logo" className="TitleLogo" />
            <LabeledInput label="Username" type="text" placeholder="Black Widow" value={username} onChange={setUsername} autofocus={true} />
            <LabeledInput label="Password" type="password" placeholder="asd123" value={password} onChange={setPassword} />
            {isRegister && <LabeledInput label="Email" type="email" placeholder="176246@bme.hu" value={email} onChange={setEmail} />}
        </div>
        <div class="LoginFooter">
            <button type="button" onClick={()=> loginRegister()}>
                {isRegister ? 'Register' : 'Login'}
            </button>
            <LinkText text={isRegister ? 'Already have an account? Login' : 'Don’t have an account? Register'}
                onClick={() => setRegister(!isRegister)} />
        </div>
    </div>
}