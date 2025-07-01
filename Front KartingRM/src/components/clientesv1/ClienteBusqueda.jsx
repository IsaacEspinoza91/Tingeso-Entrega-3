import React from 'react'
import './ClienteBusqueda.css'
import {
    FaSearch,
    FaListUl,
    FaUserPlus,
    FaUsers,
    FaUserSlash
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

    const mostrarVerTodos = mostrarBotonVerTodos &&
        (!mostrarInactivos || hayResultadosFiltrados || searchTerm.trim() !== '');

    return (
        <div className="cliente-busqueda-container">
            <div className="busqueda-superior">
                <div className="busqueda-input-container">
                    <label htmlFor="clienteSearch" className="search-label">Buscar Cliente:</label>
                    <select
                        value={searchType}
                        onChange={(e) => setSearchType(e.target.value)}
                        className="busqueda-select"
                    >
                        <option value="nombre">Por Nombre</option>
                        <option value="rut">Por RUT</option>
                        <option value="id">Por ID</option>
                    </select>

                    <input
                        id="clienteSearch"
                        type="text"
                        placeholder={`Ingrese ${searchType === 'id' ? 'ID' : searchType === 'rut' ? 'RUT' : 'nombre'}...`}
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        onKeyPress={(e) => e.key === 'Enter' && onBuscar()}
                        className="busqueda-input"
                    />

                    <button className="buscar-btn" onClick={onBuscar}>
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