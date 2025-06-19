import React, { useState, useEffect } from 'react';
import './ComprobanteDetails.css';

const ComprobanteDetails = ({ comprobante }) => {
    const [detalles, setDetalles] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (comprobante && comprobante.detalles) {
            setDetalles(comprobante.detalles);
        }
    }, [comprobante]);

    const handleUpdateDetalle = (updatedDetalle) => {
        setDetalles(detalles.map(d =>
            d.id === updatedDetalle.id ? updatedDetalle : d
        ));
    };


    if (!comprobante) return null;
    if (loading) return <div className="loading">Cargando detalles...</div>;
    if (error) return <div className="error">Error: {error}</div>;

    return (
        <div className="comprobante-details">
            <h3>Detalles del Comprobante #{comprobante.id}</h3>

            <div className="table-responsive">
                <table className="detalles-table">
                    <thead>
                        <tr>
                            <th>Cliente</th>
                            <th>Tarifa</th>
                            <th>% D.G</th>
                            <th>Desc. Grupo</th>
                            <th>% D.E</th>
                            <th>Desc. Especial</th>
                            <th>Desc. Extra</th>
                            <th>Monto Final</th>
                            <th>IVA</th>
                            <th>Total</th>
                        </tr>
                    </thead>
                    <tbody>
                        {detalles.length > 0 ? (
                            detalles.map((detalle) => (
                                <tr key={detalle.id}>
                                    <td>
                                        {detalle.cliente.nombre} {detalle.cliente.apellido}
                                        <br />
                                        <small>(ID: {detalle.cliente.id})</small>
                                        {detalle.tieneDescuentoCumpleanios && (
                                            <div className="badge cumpleanios"> Cumpleaños</div>
                                        )}
                                        {detalle.tieneDescuentoClienteFrecuente && (
                                            <div className="badge frecuente"> Frecuente</div>
                                        )}
                                    </td>
                                    <td>${detalle.tarifa.toLocaleString()}</td>
                                    <td>{detalle.porcentajeDescuentoGrupo}%</td>
                                    <td>${detalle.descuentoGrupo.toLocaleString()}</td>
                                    <td>{detalle.porcentajeDescuentoEspecial}%</td>
                                    <td>${detalle.descuentoEspecial.toLocaleString()}</td>
                                    <td>${detalle.descuentoExtra.toLocaleString()}</td>
                                    <td>${detalle.montoFinal.toLocaleString()}</td>
                                    <td>${detalle.montoIva.toLocaleString()}</td>
                                    <td>${detalle.montoTotal.toLocaleString()}</td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan="11" className="no-results">
                                    No hay detalles registrados para este comprobante
                                </td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default ComprobanteDetails;