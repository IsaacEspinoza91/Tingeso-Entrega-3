import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { getPlanes, getPlanById } from '../../services/planService'
import PlanesList from '../../components/planes/PlanesList'
import PlanForm from '../../components/planes/PlanForm'
import './PlanesPage.css'
import { FaPlus, FaSearch, FaHome, FaListUl, FaExclamationTriangle } from 'react-icons/fa'

export default function PlanesPage() {
    const [planes, setPlanes] = useState([])
    const [loading, setLoading] = useState(true)
    const [searchId, setSearchId] = useState('')
    const [showForm, setShowForm] = useState(false)
    const [currentPlan, setCurrentPlan] = useState(null)
    const [inputError, setInputError] = useState(false);
    const navigate = useNavigate();



    useEffect(() => {
        fetchPlanes()
    }, [])

    const fetchPlanes = async () => {
        setLoading(true)
        try {
            const data = await getPlanes()
            setPlanes(data)
        } catch (error) {
            console.error('Error:', error)
        } finally {
            setLoading(false)
        }
    }

    const handleKeyDown = (e) => {
        if (!/\D|Backspace|Delete|ArrowLeft|ArrowRight|Tab|Control|Meta|Shift|Alt/.test(e.key)) {
            setInputError(true);
            e.preventDefault();
        }
    };

    const handleSearch = async () => {
        if (!searchId) return fetchPlanes()

        if (!/^\d+$/.test(searchId)) {
            alert('Por favor ingrese solo números positivos');
            return;
        }

        setLoading(true)
        try {
            const plan = await getPlanById(searchId)
            setPlanes(plan ? [plan] : [])
        } catch (error) {
            console.error('Error:', error)
            setPlanes([])
        } finally {
            setLoading(false)
        }
    }

    const handleEdit = (plan) => {
        setCurrentPlan(plan)
        setShowForm(true)
    }

    const handleFormClose = () => {
        setShowForm(false)
        setCurrentPlan(null)
        fetchPlanes()
    }

    return (
        <div className="planes-container">
            <div className="planes-header">
                <div className="content-header">
                    <button
                        onClick={() => navigate('/')}
                        className="floating-home-btn"
                        aria-label="Volver al inicio"
                    >
                        <FaHome />
                        <span>Inicio</span>
                    </button>
                    <h1>Gestión de Planes</h1>
                </div>

                <div className="search-container">
                    <div className="search-bar">
                        <div className="search-group">
                            <label htmlFor="planSearch" className="search-label">Buscar Plan:</label>
                            <div className="input-container">
                                <input
                                    id="planSearch"
                                    type="text"
                                    placeholder="Ingrese ID..."
                                    value={searchId}
                                    onChange={(e) => {
                                        const validatedValue = e.target.value.replace(/\D/g, '');
                                        setSearchId(validatedValue);
                                        setInputError(false);
                                    }}
                                    onKeyDown={handleKeyDown}
                                />
                                {inputError && (
                                    <div className="error-tooltip">
                                        <FaExclamationTriangle className="error-icon" />
                                        Solo se permiten números positivos
                                    </div>
                                )}
                            </div>

                            <div className="search-actions">
                                <button onClick={handleSearch} className="search-btn">
                                    <FaSearch className="btn-icon" />
                                    Buscar
                                </button>
                                {searchId && (
                                    <button
                                        onClick={() => {
                                            setSearchId('');
                                            fetchPlanes();
                                        }}
                                        className="show-all-btn"
                                    >
                                        <FaListUl className="btn-icon" />
                                        Ver Todos
                                    </button>
                                )}
                            </div>
                        </div>
                        <button onClick={() => setShowForm(true)} className="add-btn">
                            <FaPlus className="btn-icon" />
                            Crear Plan
                        </button>
                    </div>
                </div>
            </div>

            <PlanesList
                planes={planes}
                loading={loading}
                onEdit={handleEdit}
                refreshPlanes={fetchPlanes}
            />

            {showForm && (
                <PlanForm
                    plan={currentPlan}
                    onClose={handleFormClose}
                />
            )}
        </div>
    )
}