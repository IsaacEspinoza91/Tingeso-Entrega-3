import React, { useState } from 'react'
import './ClienteBusqueda.css'
import {
    FaSearch,
    FaListUl,
    FaUserPlus,
    FaUsers,
    FaUserSlash,
    FaExclamationTriangle
} from 'react-icons/fa'

export default function ClienteBusqueda({
    searchTerm,
    setSearchTerm,
    searchType,
    setSearchType,
    onBuscar,
    onReset,
    onToggleInactivos,
    onNuevoCliente,
    mostrarBotonVerTodos = true,
    mostrarInactivos = false,
    hayResultadosFiltrados
}) {
    const [error, setError] = useState(null)
    const [inputRef, setInputRef] = useState(null)

    const mostrarVerTodos = mostrarBotonVerTodos &&
        (!mostrarInactivos || hayResultadosFiltrados || searchTerm.trim() !== '');

    const handleInputChange = (e) => {
        const value = e.target.value;
        let newError = null;

        switch (searchType) {
            case 'nombre':
                if (!/^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\s'-]*$/.test(value)) {
                    newError = 'Solo se permiten letras';
                }
                break;

            case 'rut':
                if (!/^[\d\.-]*$/.test(value)) {
                    newError = 'Solo se permiten números, punto y guión';
                } else if (value.length > 12) {
                    newError = 'Máximo 12 caracteres';
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
        <div className="cliente-busqueda-container">
            <div className="busqueda-superior">
                <div className="busqueda-input-container">
                    <label htmlFor="clienteSearch" className="search-label">Buscar Cliente:</label>
                    <select
                        value={searchType}
                        onChange={handleSearchTypeChange}
                        className="busqueda-select"
                    >
                        <option value="nombre">Por Nombre</option>
                        <option value="rut">Por RUT</option>
                        <option value="id">Por ID</option>
                    </select>

                    <div className="input-with-error-container">
                        <input
                            id="clienteSearch"
                            type="text"
                            placeholder={`Ingrese ${searchType === 'id' ? 'ID' : searchType === 'rut' ? 'RUT (máx. 12 caracteres)' : 'nombre'}...`}
                            value={searchTerm}
                            onChange={handleInputChange}
                            onKeyPress={handleKeyPress}
                            className="busqueda-input"
                            maxLength={searchType === 'rut' ? 12 : undefined}
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
                    {mostrarVerTodos && (
                        <button
                            className="ver-todos-btn"
                            onClick={onReset}
                            title="Restablecer la vista a todos los clientes"
                        >
                            <FaListUl className="btn-icon" />
                            Ver Todos
                        </button>
                    )}

                    <button
                        onClick={onToggleInactivos}
                        className={`inactivos-btn ${mostrarInactivos ? 'inactive' : ''}`}
                    >
                        {mostrarInactivos ? (
                            <>
                                <FaUsers className="btn-icon" />
                                Ver Activos
                            </>
                        ) : (
                            <>
                                <FaUserSlash className="btn-icon" />
                                Ver Inactivos
                            </>
                        )}
                    </button>

                    <button className="nuevo-btn" onClick={onNuevoCliente}>
                        <FaUserPlus className="btn-icon" />
                        Nuevo Cliente
                    </button>
                </div>
            </div>
        </div>
    )
}