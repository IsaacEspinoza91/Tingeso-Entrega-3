import { useState } from 'react';
import HolidayConfig from './HolidayConfig';
import GroupDiscounts from './GroupDiscounts';
import FrequentCustomers from './FrequentCustomers';
import './SettingsMenu.css';

const SettingsMenu = () => {
    const [activeView, setActiveView] = useState('menu');

    const renderView = () => {
        switch (activeView) {
            case 'holidays':
                return <HolidayConfig onBack={() => setActiveView('menu')} />;
            case 'groupDiscounts':
                return <GroupDiscounts onBack={() => setActiveView('menu')} />;
            case 'frequentCustomers':
                return <FrequentCustomers onBack={() => setActiveView('menu')} />;
            default:
                return (
                    <div className="settings-grid">
                        <button
                            className="settings-button"
                            onClick={() => setActiveView('holidays')}
                        >
                            <h1>Definición Días Feriados y Especiales</h1>
                        </button>
                        <button
                            className="settings-button"
                            onClick={() => setActiveView('groupDiscounts')}
                        >
                            <h1>Configuración Descuentos de Grupo</h1>
                        </button>
                        <button
                            className="settings-button"
                            onClick={() => setActiveView('frequentCustomers')}
                        >
                            <h1>Configuración Descuentos de Frecuencia</h1>
                        </button>
                    </div>
                );
        }
    };

    return (
        <div className="settings-container">
            {renderView()}
        </div>
    );
};

export default SettingsMenu;