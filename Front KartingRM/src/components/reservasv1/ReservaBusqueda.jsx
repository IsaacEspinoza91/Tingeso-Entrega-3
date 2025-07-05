import React, { useState } from 'react'
import './ReservaBusqueda.css'
import {
    FaSearch,
    FaListUl,
    FaPlus,
    FaExclamationTriangle
} from 'react-icons/fa'

export default function ReservaBusqueda({
    searchTerm,
    setSearchTerm,
    searchType,
    setSearchType,
    onBuscar,
    onReset,
    onNuevaReserva
}) {
    const [error, setError] = useState(null)


    const handleInputChange = (e) => {
        const value = e.target.value;
        let newError = null;

        switch (searchType) {
            case 'nombre':
                if (!/^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\s'-]*$/.test(value)) {
                    newError = 'Solo se permiten letras';
                }
                break;

            case 'fecha':
                if (value && !/^[\d-]*$/.test(value)) {
                    newError = 'Solo se permiten números y guiones';
                } else if (value.length > 10) {
                    newError = 'Máximo 10 caracteres (YYYY-MM-DD)';
                }
                break;
            case 'id':
                if (!/^\d*$/.test(value)) {
                    newError = 'Solo se permiten números positivos';
                }
                break;
        }

        setError(newError);
        if (!newError) {
            setSearchTerm(value);
        }
    };

    const handleKeyPress = (e) => {
        if (e.key === 'Enter' && searchTerm.trim() && !error) {
            onBuscar();
        }
    };

    const handleSearchTypeChange = (e) => {
        setSearchType(e.target.value);
        setSearchTerm('');
        setError(null);
    };

    return (
        <div className="reserva-busqueda-container">
            <div className="busqueda-superior">
                <div className="busqueda-input-container">
                    <label htmlFor="reservaSearch" className="search-label">Buscar Reserva:</label>
                    <select
                        value={searchType}
                        onChange={handleSearchTypeChange}
                        className="busqueda-select"
                    >
                        <option value="id">Por ID</option>
                        <option value="nombre">Por Nombre Cliente</option>
                        <option value="fecha">Por Fecha</option>
                    </select>

                    <div className="input-with-error-container">
                        <input
                            id="reservaSearch"
                            type={searchType === 'fecha' ? 'date' : 'text'}
                            placeholder={
                                searchType === 'id' ? 'Ingrese ID...' :
                                    searchType === 'fecha' ? 'YYYY-MM-DD' :
                                        'Ingrese nombre del cliente...'
                            }
                            value={searchTerm}
                            onChange={handleInputChange}
                            onKeyPress={handleKeyPress}
                            className="busqueda-input"
                        />
                        {error && (
                            <div className="error-tooltip">
                                <FaExclamationTriangle className="error-icon" />
                                {error}
                            </div>
                        )}
                    </div>

                    <button
                        className="buscar-btn"
                        onClick={() => !error && onBuscar()}
                        disabled={!searchTerm.trim() || error}
                    >
                        <FaSearch className="btn-icon" />
                        Buscar
                    </button>
                </div>
            </div>

            <div className="busqueda-inferior">
                <div className="botones-derecha">
                    <button
                        className="ver-todos-btn"
                        onClick={onReset}
                        title="Mostrar todas las reservas"
                    >
                        <FaListUl className="btn-icon" />
                        Ver Todos
                    </button>

                    <button className="nuevo-btn" onClick={onNuevaReserva}>
                        <FaPlus className="btn-icon" />
                        Nueva Reserva
                    </button>
                </div>
            </div>
        </div>
    )
}