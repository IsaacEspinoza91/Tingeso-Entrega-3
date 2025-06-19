import React from 'react';
import './DeleteConfirmationModal.css';

const DeleteConfirmationModal = ({
    item,
    itemType = 'comprobante',
    onClose,
    onConfirm,
    isDeleting = false
}) => {
    if (!item) return null;

    // Función para determinar qué información mostrar del item
    const getItemIdentifier = () => {
        if (itemType === 'comprobante') {
            return `ID ${item.id} (Total: $${item.total?.toLocaleString() || '0'})`;
        }
        if (itemType === 'detalle') {
            return `ID ${item.id} (Cliente: ${item.cliente?.nombre || 'N/A'})`;
        }
        if (itemType === 'reserva') {
            return `ID ${item.id} (Fecha: ${item.fecha})`;
        }
        return item.nombre || item.modelo || `ID ${item.id}`;
    };

    return (
        <div className="modal-overlay">
            <div className="delete-confirmation-modal">
                <div className="modal-header">
                    <h3>Confirmar Eliminación</h3>
                    <button
                        onClick={onClose}
                        className="close-button"
                        disabled={isDeleting}
                    >
                        &times;
                    </button>
                </div>

                <div className="confirmation-message">
                    <p>¿Estás seguro que deseas eliminar este {itemType}?</p>
                    <p className="item-details">{getItemIdentifier()}</p>
                    {isDeleting && <div className="deleting-status">Eliminando...</div>}
                </div>

                <div className="modal-actions">
                    <button
                        type="button"
                        onClick={onClose}
                        className="cancel-button"
                        disabled={isDeleting}
                    >
                        Cancelar
                    </button>
                    <button
                        type="button"
                        onClick={() => onConfirm(item.id)}
                        className="delete-button"
                        disabled={isDeleting}
                    >
                        {isDeleting ? 'Eliminando...' : 'Confirmar Eliminación'}
                    </button>
                </div>
            </div>
        </div>
    );
};

export default DeleteConfirmationModal;