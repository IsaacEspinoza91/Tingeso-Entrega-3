import { useState, useEffect } from 'react';
import {
    getAllGroupDiscounts,
    createGroupDiscount,
    updateGroupDiscount,
    deleteGroupDiscount
} from '../../services/descuentosGrupo';
import './GroupDiscounts.css';

const GroupDiscounts = ({ onBack }) => {
    const [discounts, setDiscounts] = useState([]);
    const [showForm, setShowForm] = useState(false);
    const [formData, setFormData] = useState({
        minPersonas: '',
        maxPersonas: '',
        porcentajeDescuento: ''
    });
    const [editingId, setEditingId] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        loadDiscounts();
    }, []);

    const loadDiscounts = async () => {
        setLoading(true);
        setError(null);
        try {
            const data = await getAllGroupDiscounts();
            setDiscounts(data);
        } catch (err) {
            setError('Error al cargar descuentos de grupo');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        try {
            const numericData = {
                minPersonas: parseInt(formData.minPersonas),
                maxPersonas: parseInt(formData.maxPersonas),
                porcentajeDescuento: parseFloat(formData.porcentajeDescuento)
            };

            if (editingId) {
                await updateGroupDiscount(editingId, numericData);
            } else {
                await createGroupDiscount(numericData);
            }
            setShowForm(false);
            setFormData({ minPersonas: '', maxPersonas: '', porcentajeDescuento: '' });
            setEditingId(null);
            await loadDiscounts();
        } catch (err) {
            setError(editingId ? 'Error al actualizar descuento' : 'Error al crear descuento');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const handleEdit = (discount) => {
        setFormData({
            minPersonas: discount.minPersonas.toString(),
            maxPersonas: discount.maxPersonas.toString(),
            porcentajeDescuento: discount.porcentajeDescuento.toString()
        });
        setEditingId(discount.id);
        setShowForm(true);
    };

    const handleDelete = async (id) => {
        if (!window.confirm('¿Estás seguro de eliminar este descuento?')) return;

        setLoading(true);
        setError(null);
        try {
            await deleteGroupDiscount(id);
            await loadDiscounts();
        } catch (err) {
            setError('Error al eliminar descuento');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const formatPercentage = (value) => {
        return `${(value * 100).toFixed(1)}%`;
    };

    return (
        <div className="group-discounts-container">
            <button className="back-button" onClick={onBack}>
                ←
            </button>

            <div className="discounts-header">
                <h2>Configuración Descuentos de Grupo</h2>

                <div className="controls">
                    <button
                        className="add-button"
                        onClick={() => {
                            setShowForm(true);
                            setEditingId(null);
                            setFormData({ minPersonas: '', maxPersonas: '', porcentajeDescuento: '' });
                        }}
                        disabled={loading}
                    >
                        + Añadir Descuento
                    </button>
                </div>
            </div>

            {error && <div className="error-message">{error}</div>}

            {showForm && (
                <div className="form-modal">
                    <div className="form-container">
                        <h3>{editingId ? 'Editar Descuento' : 'Nuevo Descuento'}</h3>
                        <form onSubmit={handleSubmit}>
                            <div className="form-group">
                                <label>Mínimo de personas:</label>
                                <input
                                    type="number"
                                    name="minPersonas"
                                    min="1"
                                    value={formData.minPersonas}
                                    onChange={handleInputChange}
                                    required
                                    disabled={loading}
                                />
                            </div>
                            <div className="form-group">
                                <label>Máximo de personas:</label>
                                <input
                                    type="number"
                                    name="maxPersonas"
                                    min={formData.minPersonas || 1}
                                    value={formData.maxPersonas}
                                    onChange={handleInputChange}
                                    required
                                    disabled={loading}
                                />
                            </div>
                            <div className="form-group">
                                <label>Porcentaje de descuento:</label>
                                <div className="percentage-input">
                                    <input
                                        type="number"
                                        name="porcentajeDescuento"
                                        step="0.01"
                                        min="0"
                                        max="100"
                                        value={formData.porcentajeDescuento}
                                        onChange={handleInputChange}
                                        required
                                        disabled={loading}
                                    />
                                    <span>%</span>
                                </div>
                            </div>
                            <div className="form-actions">
                                <button type="submit" disabled={loading}>
                                    {loading ? 'Guardando...' : 'Guardar'}
                                </button>
                                <button
                                    type="button"
                                    onClick={() => setShowForm(false)}
                                    disabled={loading}
                                >
                                    Cancelar
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}

            <div className="discounts-list-container">
                <div className="discounts-list-header">
                    <h3>Todos los Descuentos de Grupo</h3>
                    <span className="total-count">{discounts.length} registros</span>
                </div>

                {loading && !showForm ? (
                    <div className="loading-message">Cargando descuentos...</div>
                ) : discounts.length === 0 ? (
                    <div className="empty-message">No hay descuentos registrados</div>
                ) : (
                    <div className="table-container">
                        <table>
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Mín. Personas</th>
                                    <th>Máx. Personas</th>
                                    <th>Descuento</th>
                                    <th>Acciones</th>
                                </tr>
                            </thead>
                            <tbody>
                                {discounts.map((discount) => (
                                    <tr key={discount.id}>
                                        <td>{discount.id}</td>
                                        <td>{discount.minPersonas}</td>
                                        <td>{discount.maxPersonas}</td>
                                        <td>{formatPercentage(discount.porcentajeDescuento)}</td>
                                        <td className="actions-cell">
                                            <button
                                                className="edit-button"
                                                onClick={() => handleEdit(discount)}
                                                disabled={loading}
                                            >
                                                Editar
                                            </button>
                                            <button
                                                className="delete-button"
                                                onClick={() => handleDelete(discount.id)}
                                                disabled={loading}
                                            >
                                                Eliminar
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                )}
            </div>
        </div>
    );
};

export default GroupDiscounts;