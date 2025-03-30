import './LabeledInput.less';

/**
 * Props for the LabeledInput component.
 */
export type LabeledInputProps = {
    label: string;
    type: "text" | "password" | "email";
    placeholder: string;
    value: string;
    onChange: (s: string) => void;
    autofocus?: boolean;
}

/**
 * Renders a labeled input field.
 * @param label - The label for the input field.
 * @param type - The type of the input field.
 * @param placeholder - The placeholder text for the input field.
 * @param value - The current value of the input field.
 * @param onChange - Function to call when the input value changes.
 * @param autofocus - Whether the input should be focused automatically.
 */
export function LabeledInput({ label, type, placeholder, value, onChange, autofocus }: Readonly<LabeledInputProps>) {    
    return <div class="LabeledInput">
        <label>{label}</label>
        <input type={type} placeholder={placeholder} value={value} onInput={e => onChange(e.currentTarget.value)} autofocus={autofocus} />
    </div>
}