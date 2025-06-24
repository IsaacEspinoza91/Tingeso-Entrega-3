import { useState, useEffect } from 'react'
import { createPlan, updatePlan } from '../../services/planService'
import { FaSave, FaPlus, FaClock, FaMoneyBillWave, FaTimes, FaListAlt } from 'react-icons/fa';

export default function PlanForm({ plan, onClose }) {
    const [formData, setFormData] = useState({
        descripcion: '',
        duracionTotal: 1,
        precioRegular: 0,
        precioFinSemana: 0,
        precioFeriado: 0
    })
    const [loading, setLoading] = useState(false)

    useEffect(() => {
        if (plan) {
            setFormData({
                descripcion: plan.descripcion,
                duracionTotal: plan.duracionTotal,
                precioRegular: plan.precioRegular,
                precioFinSemana: plan.precioFinSemana,
                precioFeriado: plan.precioFeriado
            })
        }
    }, [plan])

    const handleChange = (e) => {
        const { name, value } = e.target
        setFormData(prev => ({
            ...prev,
            [name]: name.includes('precio') || name === 'duracionTotal'
                ? parseFloat(value) || 0
                : value
        }))
    }

    const handleSubmit = async (e) => {
        e.preventDefault()
        setLoading(true)
        try {
            if (plan) {
                await updatePlan(plan.id, formData)
            } else {
                await createPlan(formData)
            }
            onClose()
        } catch (error) {
            console.error('Error:', error)
        } finally {
            setLoading(false)
        }
    }

    return (
        <div className="modal-overlay">
            <div className="plan-form-modal">
                <h2>{plan ? 'Editar Plan' : 'Crear Nuevo Plan'}</h2>
                <button className="close-btn" onClick={onClose}>×</button>

                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>
                            <FaListAlt className='input-icon' />Descripción:</label>
                        <input
                            type="text"
                            name="descripcion"
                            value={formData.descripcion}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label>
                            <FaClock className="input-icon" /> Duración (minutos):
                        </label>
                        <input
                            type="number"
                            name="duracionTotal"
                            min="1"
                            value={formData.duracionTotal}
                            onChange={handleChange}
                            required
                        />
                        {formData.duracionTotal <= 0 && (
                            <span className="error-message">La duración debe ser mayor a 0</span>
                        )}
                    </div>

                    <div className="form-group">
                        <label>
                            <FaMoneyBillWave className="input-icon" /> Precio Regular:
                        </label>
                        <input
                            type="number"
                            name="precioRegular"
                            min="0"
                            step="1"
                            value={formData.precioRegular}
                            onChange={handleChange}
                            required
                        />
                        {formData.precioRegular < 0 && (
                            <span className="error-message">El precio no puede ser negativo</span>
                        )}
                    </div>

                    <div className="form-group">
                        <label>
                            <FaMoneyBillWave className="input-icon" /> Precio Fin de Semana:
                        </label>
                        <input
                            type="number"
                            name="precioFinSemana"
                            min="0"
                            step="1"
                            value={formData.precioFinSemana}
                            onChange={handleChange}
                            required
                        />
                        {formData.precioFinSemana < 0 && (
                            <span className="error-message">El precio no puede ser negativo</span>
                        )}
                    </div>

                    <div className="form-group">
                        <label>
                            <FaMoneyBillWave className="input-icon" /> Precio Feriado:
                        </label>
                        <input
                            type="number"
                            name="precioFeriado"
                            min="0"
                            step="1"
                            value={formData.precioFeriado}
                            onChange={handleChange}
                            required
                        />
                        {formData.precioFeriado < 0 && (
                            <span className="error-message">El precio no puede ser negativo</span>
                        )}
                    </div>


                    <div className="form-actions">
                        <button onClick={onClose} className="cancel-btn">
                            <FaTimes className="btn-icon" />
                            Cancelar
                        </button>
                        <button disabled={loading} className="submit-btn">
                            {loading ? (
                                <span>Procesando...</span>
                            ) : plan ? (
                                <>
                                    <FaSave className="btn-icon" />
                                    <span>Actualizar Plan</span>
                                </>
                            ) : (
                                <>
                                    <FaPlus className="btn-icon" />
                                    <span>Crear Plan</span>
                                </>
                            )}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    )
}