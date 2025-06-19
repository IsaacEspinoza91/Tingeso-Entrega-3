import { useState, useEffect } from 'react';
import {
    getAllFrequentCustomerDiscounts,
    createFrequentCustomerDiscount,
    updateFrequentCustomerDiscount,
    deleteFrequentCustomerDiscount
} from '../../services/descuentosClienteFrecuente';
import './FrequentCustomers.css';

const FrequentCustomers = ({ onBack }) => {
    const [discounts, setDiscounts] = useState([]);
    const [showForm, setShowForm] = useState(false);
    const [formData, setFormData] = useState({
        minReservas: '',
        maxReservas: '',
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
            const data = await getAllFrequentCustomerDiscounts();
            setDiscounts(data);
        } catch (err) {
            setError('Error al cargar descuentos para clientes frecuentes');
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
                minReservas: parseInt(formData.minReservas),
                maxReservas: parseInt(formData.maxReservas),
                porcentajeDescuento: parseFloat(formData.porcentajeDescuento)
            };

            if (editingId) {
                await updateFrequentCustomerDiscount(editingId, numericData);
            } else {
                await createFrequentCustomerDiscount(numericData);
            }
            setShowForm(false);
            setFormData({ minReservas: '', maxReservas: '', porcentajeDescuento: '' });
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
            minReservas: discount.minReservas.toString(),
            maxReservas: discount.maxReservas.toString(),
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
            await deleteFrequentCustomerDiscount(id);
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
        <div className="frequent-customers-container">
            <button className="back-button" onClick={onBack}>
                ←
            </button>

            <div className="discounts-header">
                <h2>Configuración Descuentos de Clientes Frecuentes</h2>

                <div className="controls">
                    <button
                        className="add-button"
                        onClick={() => {
                            setShowForm(true);
                            setEditingId(null);
                            setFormData({ minReservas: '', maxReservas: '', porcentajeDescuento: '' });
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
                                <label>Mínimo de reservas:</label>
                                <input
                                    type="number"
                                    name="minReservas"
                                    min="1"
                                    value={formData.minReservas}
                                    onChange={handleInputChange}
                                    required
                                    disabled={loading}
                                />
                            </div>
                            <div className="form-group">
                                <label>Máximo de reservas:</label>
                                <input
                                    type="number"
                                    name="maxReservas"
                                    min={formData.minReservas || 1}
                                    value={formData.maxReservas}
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
                                        step="0.1"
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
                    <h3>Todos los Descuentos para Clientes Frecuentes</h3>
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
                                    <th>Mín. Reservas</th>
                                    <th>Máx. Reservas</th>
                                    <th>Descuento</th>
                                    <th>Acciones</th>
                                </tr>
                            </thead>
                            <tbody>
                                {discounts.map((discount) => (
                                    <tr key={discount.id}>
                                        <td>{discount.id}</td>
                                        <td>{discount.minReservas}</td>
                                        <td>{discount.maxReservas}</td>
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

export default FrequentCustomers;