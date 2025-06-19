import React, { useState } from 'react';
import { getComprobanteById, getComprobanteByIdReserva, deleteComprobante } from '../../services/comprobanteService';
import ComprobanteDetails from './ComprobanteDetails';
import PDFDownloadButton from './ComprobantePDF/PDFDownloadButton';
import CreateComprobanteModal from './CreateComprobanteModal';
import DeleteConfirmationModal from './DeleteConfirmationModal';
import EditComprobanteModal from './EditComprobanteModal';
import EmailModal from './ComprobantePDF/EmailModal';
import './ComprobanteSearch.css';

const ComprobanteSearch = () => {
    const [searchValue, setSearchValue] = useState('');
    const [searchType, setSearchType] = useState('reserva'); // 'id' o 'reserva'
    const [isSearching, setIsSearching] = useState(false);
    const [isDeleting, setIsDeleting] = useState(false);
    const [error, setError] = useState('');
    const [comprobante, setComprobante] = useState(null);
    const [showCreateModal, setShowCreateModal] = useState(false);
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [showEditModal, setShowEditModal] = useState(false);
    const [showEmailModal, setShowEmailModal] = useState(false);

    const handleSearch = async (e) => {
        e.preventDefault();

        if (!searchValue.trim()) {
            setError(`Por favor ingrese un ID de ${searchType === 'id' ? 'comprobante' : 'reserva'}`);
            return;
        }

        setIsSearching(true);
        setError('');
        setComprobante(null);

        try {
            if (searchType === 'id') {
                const result = await getComprobanteById(searchValue);
                setComprobante(result);
            } else {
                const result = await getComprobanteByIdReserva(searchValue);
                setComprobante(result);
            }
        } catch (err) {
            if (err.response && err.response.status === 404) {
                setError(err.response.data);
            } else {
                if (searchType === 'id') {
                    setError('No se encontró un comprobante con la ID ');
                } else {
                    setError('No se encontró un comprobante con la ID de reserva ');
                }
            }
            console.error('Error al buscar comprobante:', err);
        } finally {
            setIsSearching(false);
        }
    };

    const handleReset = () => {
        setSearchValue('');
        setError('');
        setComprobante(null);
    };

    const handleComprobanteCreated = (newComprobante) => {
        setComprobante(newComprobante);
        setShowCreateModal(false);
        setSearchValue(newComprobante.id.toString());
    };

    const handleDelete = async () => {
        setIsDeleting(true);
        try {
            await deleteComprobante(comprobante.id);
            setComprobante(null);
            setSearchValue('');
        } catch (err) {
            setError('Error al eliminar el comprobante');
            console.error('Error al eliminar comprobante:', err);
        } finally {
            setIsDeleting(false);
            setShowDeleteModal(false);
        }
    };

    const handleSendComprobante = () => {
        setShowEmailModal(true);
    };

    const handleUpdateComprobante = (updatedComprobante) => {
        setComprobante(updatedComprobante);
        setShowEditModal(false);
    };


    return (
        <div className="comprobante-search-container">
            <div className="search-header">
                <h3>Comprobantes</h3>
                <div className="header-actions">
                    <button
                        onClick={() => setShowCreateModal(true)}
                        className="create-button"
                    >
                        Crear Nuevo Comprobante
                    </button>
                </div>
            </div>

            <div className="search-section">
                <h4>Buscar Comprobante</h4>
                <form onSubmit={handleSearch}>
                    <div className="search-options">
                        <label>
                            <input
                                type="radio"
                                value="id"
                                checked={searchType === 'id'}
                                onChange={() => setSearchType('id')}
                            />
                            Por ID de Comprobante
                        </label>
                        <label>
                            <input
                                type="radio"
                                value="reserva"
                                checked={searchType === 'reserva'}
                                onChange={() => setSearchType('reserva')}
                            />
                            Por ID de Reserva
                        </label>
                    </div>
                    <div className="search-controls">
                        <div className="search-input">
                            <input
                                type="number"
                                value={searchValue}
                                onChange={(e) => {
                                    const value = e.target.value;
                                    if (value === '' || /^[0-9]*$/.test(value)) {
                                        setSearchValue(value);
                                    }
                                }}
                                placeholder={`Ingrese ID de ${searchType === 'id' ? 'comprobante' : 'reserva'}`}
                                min="1"
                            />
                            <button
                                type="submit"
                                disabled={isSearching || !searchValue.trim()}
                                className="search-button"
                            >
                                {isSearching ? 'Buscando...' : 'Buscar'}
                            </button>
                            <button
                                type="button"
                                onClick={handleReset}
                                className="reset-button"
                            >
                                Limpiar
                            </button>
                        </div>
                    </div>
                </form>

                {error && <div className="error-message">{error}</div>}
            </div>

            {isSearching && <div className="loading">Buscando comprobante...</div>}

            {comprobante && (
                <div className="comprobante-result">
                    <div className="comprobante-header">
                        <h4>Comprobante #{comprobante.id}</h4>
                        <div className="header-actions">
                            <PDFDownloadButton comprobante={comprobante} />
                            <button
                                onClick={handleSendComprobante}
                                className="send-button"
                            >
                                Enviar Comprobante
                            </button>
                            <button
                                onClick={() => setShowEditModal(true)}
                                className="edit-button"
                            >
                                Editar
                            </button>
                            <button
                                onClick={() => setShowDeleteModal(true)}
                                className="delete-button"
                            >
                                Eliminar Comprobante
                            </button>
                        </div>
                    </div>

                    <div className="comprobante-summary">
                        <div className="summary-item">
                            <span className="summary-label">Estado:</span>
                            <span className={`status-badge ${comprobante.pagado ? 'pagado' : 'pendiente'}`}>
                                {comprobante.pagado ? 'Pagado' : 'Pendiente'}
                            </span>
                        </div>
                        <div className="summary-item">
                            <span className="summary-label">Total:</span>
                            <span>${comprobante.total.toLocaleString()}</span>
                        </div>
                        <div className="summary-item">
                            <span className="summary-label">Reserva ID:</span>
                            <span>{comprobante.reserva.id}</span>
                        </div>
                        <div className="summary-item">
                            <span className="summary-label">Fecha:</span>
                            <span>{new Date(comprobante.reserva.fecha + "T00:00:00").toLocaleDateString()}</span>
                        </div>
                    </div>

                    <ComprobanteDetails comprobante={comprobante} />
                </div>
            )}

            {showCreateModal && (
                <CreateComprobanteModal
                    onClose={() => setShowCreateModal(false)}
                    onComprobanteCreated={handleComprobanteCreated}
                />
            )}

            {showDeleteModal && (
                <DeleteConfirmationModal
                    item={comprobante}
                    itemType="comprobante"
                    onClose={() => setShowDeleteModal(false)}
                    onConfirm={handleDelete}
                    isDeleting={isDeleting}
                />
            )}

            {showCreateModal && (
                <CreateComprobanteModal
                    onClose={() => setShowCreateModal(false)}
                    onComprobanteCreated={handleComprobanteCreated}
                />
            )}

            {showEditModal && (
                <EditComprobanteModal
                    comprobante={comprobante}
                    onClose={() => setShowEditModal(false)}
                    onUpdate={handleUpdateComprobante}
                />
            )}

            {showEmailModal && (
                <EmailModal
                    comprobante={comprobante}
                    onClose={() => setShowEmailModal(false)}
                />
            )}
        </div>
    );
};

export default ComprobanteSearch;