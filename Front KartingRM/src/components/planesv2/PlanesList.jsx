import { useState } from 'react'
import { FaEdit, FaTrash } from 'react-icons/fa'
import DeletePlanModal from './DeletePlanModal'

export default function PlanesList({ planes, loading, onEdit, refreshPlanes }) {
    const [planToDelete, setPlanToDelete] = useState(null)

    if (loading) return <div className="loading-message">Cargando planes...</div>
    if (planes.length === 0) return <div className="no-results">No se encontraron planes</div>

    return (
        <div className="planes-list-container">
            <table className="planes-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Descripción</th>
                        <th>Duración (min)</th>
                        <th>Precio Regular</th>
                        <th>Precio Fin de Semana</th>
                        <th>Precio Feriado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    {planes.map((plan) => (
                        <tr key={plan.id}>
                            <td>{plan.id}</td>
                            <td>{plan.descripcion}</td>
                            <td>{plan.duracionTotal}</td>
                            <td>${plan.precioRegular.toLocaleString()}</td>
                            <td>${plan.precioFinSemana.toLocaleString()}</td>
                            <td>${plan.precioFeriado.toLocaleString()}</td>
                            <td className="actions-cell">
                                <div className="tooltip-container">
                                    <button
                                        onClick={() => onEdit(plan)}
                                        className="edit-btn"
                                        aria-label="Editar plan"
                                    >
                                        <FaEdit />
                                        <span className="tooltip-text">Editar</span>
                                    </button>
                                </div>
                                <div className="tooltip-container">
                                    <button
                                        onClick={() => setPlanToDelete(plan)}
                                        className="delete-btn"
                                        aria-label="Eliminar plan"
                                    >
                                        <FaTrash />
                                        <span className="tooltip-text">Eliminar</span>
                                    </button>
                                </div>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>

            {planToDelete && (
                <DeletePlanModal
                    plan={planToDelete}
                    onClose={() => setPlanToDelete(null)}
                    onSuccess={refreshPlanes}
                />
            )}
        </div>
    )
}