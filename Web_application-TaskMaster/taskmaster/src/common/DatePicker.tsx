import { useState } from 'preact/hooks';
import './DatePicker.less';

/**
 * Props for the DatePicker component.
 */
interface DatePickerProps {
    value?: string;
    onChange: (date: string) => void;
}

/**
 * Renders a date picker input.
 * @param value - The initial date value.
 * @param onChange - Function to call when the date changes.
 */
export function DatePicker({ value, onChange }: DatePickerProps) {
    const [selectedDate, setSelectedDate] = useState(value || '');

    /**
     * Handles the date change event.
     * @param e - The event object.
     */
    const handleDateChange = (e: Event) => {
        const target = e.target as HTMLInputElement;
        const newDate = target.value;
        setSelectedDate(newDate);
        onChange(newDate);
    };

    return (
        <div class="date-picker">
            <input
                type="date"
                value={selectedDate}
                onInput={handleDateChange}
                class="date-input"
            />
        </div>
    );
}
