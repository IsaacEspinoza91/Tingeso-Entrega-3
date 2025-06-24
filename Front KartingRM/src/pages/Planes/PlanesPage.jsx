import { useState, useEffect } from 'react'
import { getPlanes, getPlanById } from '../../services/planService'
import PlanesList from '../../components/planesv2/PlanesList'
import PlanForm from '../../components/planesv2/PlanForm'
import './PlanesPage.css'
import { FaPlus, FaSearch } from 'react-icons/fa';

export default function PlanesPage() {
    const [planes, setPlanes] = useState([])
    const [loading, setLoading] = useState(true)
    const [searchId, setSearchId] = useState('')
    const [showForm, setShowForm] = useState(false)
    const [currentPlan, setCurrentPlan] = useState(null)

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

    const handleSearch = async () => {
        if (!searchId) return fetchPlanes()

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
                <h1>Gestión de Planes</h1>

                <div className="search-container">
                    <div className="search-bar">
                        <div className="search-group">
                            <input
                                type="text"
                                placeholder="Buscar por ID..."
                                value={searchId}
                                onChange={(e) => setSearchId(e.target.value)}
                            />
                            <button onClick={handleSearch} className="search-btn">
                                <FaSearch className="btn-icon" />
                                Buscar
                            </button>
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