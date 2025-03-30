import { useRef } from 'react';

/**
 * Custom hook to play a completion effect.
 * Plays a sound and triggers an animation.
 */
export function useCompletionEffect() {
    const audioRef = useRef(new Audio('/res/yay.mp3'));

    /**
     * Plays the completion effect.
     */
    const playEffect = () => {
        // Hang lejátszása
        audioRef.current.play();
        // Pipa animáció triggerelése
        const animation = document.getElementById('completion-icon');
        if (animation) {
            animation.classList.remove('hidden');
            animation.classList.add('animate');
            setTimeout(() => {
                animation.classList.remove('animate');
                animation.classList.add('hidden');
            }, 2000); // 2 másodperc múlva reset
        }
    };

    return playEffect;
}