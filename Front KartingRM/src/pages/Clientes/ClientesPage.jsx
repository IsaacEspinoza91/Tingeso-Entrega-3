import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {
    getClientes,
    getClienteById,
    createCliente,
    updateCliente,
    deleteCliente,
    activarCliente,
    desactivarCliente
} from '../../services/clienteService';
import ClientesList from '../../components/clientesv1/ClientesList';
import ClienteForm from '../../components/clientesv1/ClienteForm';
import Notification from '../../components/Notification';
import { FaHome, FaSearch, FaPlus, FaListUl } from 'react-icons/fa';
import './ClientesPage.css';

export default function ClientesPage() {
    const [clientes, setClientes] = useState([]);
    const [loading, setLoading] = useState(true);
    const [searchId, setSearchId] = useState('');
    const [showForm, setShowForm] = useState(false);
    const [currentCliente, setCurrentCliente] = useState(null);
    const [showInactive, setShowInactive] = useState(false);
    const [notification, setNotification] = useState({ show: false, message: '', type: '' });
    const navigate = useNavigate();

    useEffect(() => {
        fetchClientes();
    }, [showInactive]);

    const fetchClientes = async () => {
        setLoading(true);
        try {
            const data = await getClientes(showInactive);
            setClientes(data);
        } catch (error) {
            console.error('Error:', error);
            showNotification('Error al cargar clientes', 'error');
        } finally {
            setLoading(false);
        }
    };

    const handleSearch = async () => {
        if (!searchId) return fetchClientes();

        setLoading(true);
        try {
            const cliente = await getClienteById(searchId);
            setClientes(cliente ? [cliente] : []);
        } catch (error) {
            console.error('Error:', error);
            showNotification('Cliente no encontrado', 'error');
            setClientes([]);
        } finally {
            setLoading(false);
        }
    };

    const handleEdit = (cliente) => {
        setCurrentCliente(cliente);
        setShowForm(true);
    };

    const handleStatusChange = async (cliente) => {
        try {
            const updatedCliente = cliente.activo
                ? await desactivarCliente(cliente.id)
                : await activarCliente(cliente.id);

            setClientes(clientes.map(c =>
                c.id === updatedCliente.id ? updatedCliente : c
            ));

            showNotification(
                `Cliente ${updatedCliente.activo ? 'activado' : 'desactivado'} correctamente`,
                'success'
            );
        } catch (error) {
            console.error('Error:', error);
            showNotification('Error al cambiar estado del cliente', 'error');
        }
    };

    const handleDelete = async (id) => {
        try {
            await deleteCliente(id);
            setClientes(clientes.filter(c => c.id !== id));
            showNotification('Cliente eliminado correctamente', 'success');
        } catch (error) {
            console.error('Error:', error);
            showNotification('Error al eliminar cliente', 'error');
        }
    };

    const handleFormClose = () => {
        setShowForm(false);
        setCurrentCliente(null);
        fetchClientes();
    };

    const showNotification = (message, type) => {
        setNotification({ show: true, message, type });
        setTimeout(() => setNotification({ ...notification, show: false }), 5000);
    };

    return (
        <div className="clientes-container">
            <div className="clientes-header">
                <div className="content-header">
                    <button
                        onClick={() => navigate('/')}
                        className="floating-home-btn"
                        aria-label="Volver al inicio"
                    >
                        <FaHome />
                        <span>Inicio</span>
                    </button>
                    <h1>Gestión de Clientes</h1>
                </div>

                <div className="search-container">
                    <div className="search-bar">
                        <div className="search-group">
                            <label htmlFor="clienteSearch" className="search-label">Buscar Cliente:</label>
                            <input
                                id="clienteSearch"
                                type="text"
                                placeholder="Ingrese ID..."
                                value={searchId}
                                onChange={(e) => {
                                    const validatedValue = e.target.value.replace(/[^0-9]/g, '');
                                    setSearchId(validatedValue);
                                }}
                                onKeyDown={(e) => {
                                    if (!/[0-9]|Backspace|Delete|ArrowLeft|ArrowRight|Tab/.test(e.key)) {
                                        e.preventDefault();
                                    }
                                }}
                            />
                            <div className="search-actions">
                                <button onClick={handleSearch} className="search-btn">
                                    <FaSearch className="btn-icon" />
                                    Buscar
                                </button>
                                {searchId && (
                                    <button
                                        onClick={() => {
                                            setSearchId('');
                                            fetchClientes();
                                        }}
                                        className="show-all-btn"
                                    >
                                        <FaListUl className="btn-icon" />
                                        Ver Todos
                                    </button>
                                )}
                            </div>
                        </div>
                        <div className="clientes-actions">
                            <button
                                onClick={() => setShowInactive(!showInactive)}
                                className={`toggle-btn ${showInactive ? 'active' : ''}`}
                            >
                                {showInactive ? 'Mostrar Activos' : 'Mostrar Inactivos'}
                            </button>
                            <button onClick={() => setShowForm(true)} className="add-btn">
                                <FaPlus className="btn-icon" />
                                Crear Cliente
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            <ClientesList
                clientes={clientes}
                loading={loading}
                showInactive={showInactive}
                onEdit={handleEdit}
                onStatusChange={handleStatusChange}
                onDelete={handleDelete}
            />

            {showForm && (
                <ClienteForm
                    cliente={currentCliente}
                    onClose={handleFormClose}
                    showNotification={showNotification}
                />
            )}

            {notification.show && (
                <Notification
                    message={notification.message}
                    type={notification.type}
                    onClose={() => setNotification({ ...notification, show: false })}
                />
            )}
        </div>
    );
}