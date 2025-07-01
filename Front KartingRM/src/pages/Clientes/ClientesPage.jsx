import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import {
    getClientesActivos,
    getClientesInactivos,
    getClienteById,
    getClienteByRut,
    getClientesByNombreParcial,
    desactivarCliente,
    activarCliente
} from '../../services/clienteService'
import ClientesList from '../../components/clientesv1/ClientesList'
import ClientesForm from '../../components/clientesv1/ClientesForm'
import './ClientesPage.css'
import { FaHome } from 'react-icons/fa'
import ClienteBusqueda from '../../components/clientesv1/ClienteBusqueda'

export default function ClientesPage() {
    const [clientes, setClientes] = useState([])
    const [loading, setLoading] = useState(true)
    const [searchTerm, setSearchTerm] = useState('')
    const [searchType, setSearchType] = useState('nombre') // 'id', 'rut', 'nombre'
    const [showForm, setShowForm] = useState(false)
    const [currentCliente, setCurrentCliente] = useState(null)
    const [showInactivos, setShowInactivos] = useState(false)
    const [hayResultadosFiltrados, setHayResultadosFiltrados] = useState(false)
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
            setHayResultadosFiltrados(false) // Resetear al cargar todos los clientes
        } catch (error) {
            console.error('Error:', error)
        } finally {
            setLoading(false)
        }
    }

    const handleSearch = async () => {
        if (!searchTerm.trim()) {
            fetchClientes()
            return
        }

        setLoading(true)
        try {
            let results = []

            switch (searchType) {
                case 'id': {
                    if (!/^\d+$/.test(searchTerm)) {
                        alert('Por favor ingrese solo números para ID')
                        return
                    }
                    const clienteById = await getClienteById(searchTerm)
                    results = clienteById ? [clienteById] : []
                    break
                }
                case 'rut':
                    results = await getClienteByRut(searchTerm)
                    break
                case 'nombre':
                    results = await getClientesByNombreParcial(searchTerm)
                    break
                default:
                    results = []
            }

            setClientes(results)
            setHayResultadosFiltrados(true) // Marcar que hay resultados filtrados
        } catch (error) {
            console.error('Error:', error)
            setClientes([])
            setHayResultadosFiltrados(false)
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
        setHayResultadosFiltrados(false)
    }

    const handleReset = () => {
        setSearchTerm('')
        setHayResultadosFiltrados(false)
        fetchClientes()
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

                <ClienteBusqueda
                    searchTerm={searchTerm}
                    setSearchTerm={setSearchTerm}
                    searchType={searchType}
                    setSearchType={setSearchType}
                    onBuscar={handleSearch}
                    onReset={handleReset}
                    onToggleInactivos={handleToggleInactivos}
                    onNuevoCliente={() => setShowForm(true)}
                    mostrarBotonVerTodos={!!searchTerm || showInactivos || hayResultadosFiltrados}
                    mostrarInactivos={showInactivos}
                    hayResultadosFiltrados={hayResultadosFiltrados}
                />
            </div>

            <ClientesList
                clientes={clientes}
                loading={loading}
                onEdit={handleEdit}
                showInactivos={showInactivos}
                refreshClientes={fetchClientes}
            />

            {showForm && (
                <ClientesForm
                    cliente={currentCliente}
                    onClose={handleFormClose}
                />
            )}
        </div>
    )
}