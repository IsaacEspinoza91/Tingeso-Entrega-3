import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import {
    getClientesActivos,
    getClientesInactivos,
    getClienteById,
    getClienteByRut,
    getClientesByNombre
} from '../../services/clienteService'
import ClientesList from '../../components/clientesv1/ClientesList'
import ClientesForm from '../../components/clientesv1/ClientesForm'
import DeleteClienteModal from '../../components/clientesv1/DeleteClienteModal'
import './ClientesPage.css'
import { FaPlus, FaSearch, FaHome, FaListUl, FaUsers, FaUserSlash } from 'react-icons/fa'

export default function ClientesPage() {
    const [clientes, setClientes] = useState([])
    const [loading, setLoading] = useState(true)
    const [searchTerm, setSearchTerm] = useState('')
    const [searchType, setSearchType] = useState('nombre') // 'id', 'rut', 'nombre'
    const [showForm, setShowForm] = useState(false)
    const [currentCliente, setCurrentCliente] = useState(null)
    const [showInactivos, setShowInactivos] = useState(false)
    const [clienteToDelete, setClienteToDelete] = useState(null)
    const navigate = useNavigate()

    useEffect(() => {
        fetchClientes()
    }, [showInactivos])

    const handleToggleStatus = async (cliente) => {
        setLoading(true);
        try {
            if (cliente.activo) {
                await desactivarCliente(cliente.id);
            } else {
                await activarCliente(cliente.id);
            }
            fetchClientes();
        } catch (error) {
            console.error('Error al cambiar estado del cliente:', error);
        } finally {
            setLoading(false);
        }
    };

    const fetchClientes = async () => {
        setLoading(true)
        try {
            const data = showInactivos
                ? await getClientesInactivos()
                : await getClientesActivos()
            setClientes(data)
        } catch (error) {
            console.error('Error:', error)
        } finally {
            setLoading(false)
        }
    }

    const handleSearch = async () => {
        if (!searchTerm.trim()) return fetchClientes()

        setLoading(true)
        try {
            let results = []

            switch (searchType) {
                case 'id':
                    if (!/^\d+$/.test(searchTerm)) {
                        alert('Por favor ingrese solo números para ID')
                        return
                    }
                    const clienteById = await getClienteById(searchTerm)
                    results = clienteById ? [clienteById] : []
                    break
                case 'rut':
                    const clienteByRut = await getClienteByRut(searchTerm)
                    results = clienteByRut ? [clienteByRut] : []
                    break
                case 'nombre':
                    results = await getClientesByNombre(searchTerm)
                    break
                default:
                    results = []
            }

            setClientes(results)
        } catch (error) {
            console.error('Error:', error)
            setClientes([])
        } finally {
            setLoading(false)
        }
    }

    const handleEdit = (cliente) => {
        setCurrentCliente(cliente)
        setShowForm(true)
    }

    const handleFormClose = () => {
        setShowForm(false)
        setCurrentCliente(null)
        fetchClientes()
    }

    const handleToggleInactivos = () => {
        setShowInactivos(!showInactivos)
        setSearchTerm('')
    }

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
                            <div className="input-container">
                                <select
                                    value={searchType}
                                    onChange={(e) => setSearchType(e.target.value)}
                                    className="search-type-select"
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
                                    onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
                                />
                            </div>

                            <div className="search-actions">
                                <button onClick={handleSearch} className="search-btn">
                                    <FaSearch className="btn-icon" />
                                    Buscar
                                </button>
                                {(searchTerm || showInactivos) && (
                                    <button
                                        onClick={() => {
                                            setSearchTerm('')
                                            fetchClientes()
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
                                onClick={handleToggleInactivos}
                                className={`toggle-inactivos-btn ${showInactivos ? 'inactive' : ''}`}
                            >
                                {showInactivos ? (
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
                            <button onClick={() => setShowForm(true)} className="add-btn">
                                <FaPlus className="btn-icon" />
                                Nuevo Cliente
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            <ClientesList
                clientes={clientes}
                loading={loading}
                onEdit={handleEdit}
                onDelete={setClienteToDelete}
                showInactivos={showInactivos}
                refreshClientes={fetchClientes}
            />

            {showForm && (
                <ClientesForm
                    cliente={currentCliente}
                    onClose={handleFormClose}
                />
            )}

            {clienteToDelete && (
                <DeleteClienteModal
                    cliente={clienteToDelete}
                    onClose={() => setClienteToDelete(null)}
                    onSuccess={fetchClientes}
                />
            )}
        </div>
    )
}