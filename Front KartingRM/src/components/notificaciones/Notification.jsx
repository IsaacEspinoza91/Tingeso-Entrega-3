import { useEffect } from 'react';
import { FaCheckCircle, FaTimesCircle, FaExclamationTriangle } from 'react-icons/fa';
import './Notification.css';

export default function Notification({ message, type, onClose }) {
    useEffect(() => {
        const timer = setTimeout(() => {
            onClose();
        }, 6000);
        return () => clearTimeout(timer);
    }, [onClose]);

    const getIcon = () => {
        switch (type) {
            case 'success':
                return <FaCheckCircle className="notification-icon" />;
            case 'error':
                return <FaTimesCircle className="notification-icon" />;
            case 'warning':
                return <FaExclamationTriangle className="notification-icon" />;
            default:
                return null;
        }
    };

    return (
        <div className={`notification ${type}`}>
            {getIcon()}
            <span>{message}</span>
            <button onClick={onClose} className="notification-close">×</button>
        </div>
    );
}