import { useEffect, useState } from 'react';
import { getComprobanteByIdReserva } from '../../services/comprobanteService';
import './ReservaDetalle.css';
import { FaFilePdf, FaEnvelope, FaEdit, FaReceipt, FaUser } from 'react-icons/fa';


const ReservaDetalle = ({ reserva, onClose }) => {
    const [comprobante, setComprobante] = useState(null);

    useEffect(() => {
        const fetchComprobante = async () => {
            try {
                const data = await getComprobanteByIdReserva(reserva.id);
                setComprobante(data);
            } catch (error) {
                console.error('Error al cargar comprobante:', error);
            }
        };
        fetchComprobante();
    }, [reserva.id]);

    return (
        <div className="modal-overlay">
            <div className="modal-content wide">
                <button className="cerrar-modal" onClick={onClose}>&times;</button>

                <h2>Detalle de Reserva #{reserva.id}</h2>
                <div className="detalle-section">
                    <p><strong>Fecha:</strong> {reserva.fecha}</p>
                    <p><strong>Horario:</strong> {reserva.horaInicio} - {reserva.horaFin}</p>
                    <p><strong>Estado:</strong> {reserva.estado}</p>
                    <p><strong>Cliente:</strong> {reserva.reservante.nombre} {reserva.reservante.apellido} ({reserva.reservante.id})</p>
                    <p><strong>Plan:</strong> {reserva.plan.descripcion} ({reserva.plan.id})</p>
                    <p><strong>Total Personas:</strong> {reserva.totalPersonas}</p>
                </div>

                {comprobante && (
                    <div className="comprobante-section">
                        <div className="comprobante-header">
                            <h3>Comprobante #{comprobante.id}</h3>
                            <div className="comprobante-actions">
                                <button className="btn-pdf"><FaFilePdf /> Descargar PDF</button>
                                <button className="btn-email"><FaEnvelope /> Enviar Comprobante</button>
                                <button className="btn-editar"><FaEdit /> Editar</button>
                            </div>
                        </div>

                        <div className="comprobante-info-bar">
                            <div className={`estado-comprobante ${comprobante.pagado ? 'pagado' : 'pendiente'}`}>
                                {comprobante.pagado ? 'Pagado' : 'Pendiente'}
                            </div>
                            <div><strong>Total:</strong> ${comprobante.total.toLocaleString()}</div>
                            <div><strong>Fecha:</strong> {reserva.fecha}</div>
                        </div>

                        <table className="comprobante-detalles">
                            <thead>
                                <tr>
                                    <th>Cliente</th>
                                    <th>Tarifa</th>
                                    <th>%Desc Gr</th>
                                    <th>Desc Grupo</th>
                                    <th>%Desc Esp</th>
                                    <th>Desc Especial</th>
                                    <th>Desc Extra</th>
                                    <th>Monto Final</th>
                                    <th>IVA</th>
                                    <th>Total</th>
                                </tr>
                            </thead>
                            <tbody>
                                {comprobante.detalles.map((det) => (
                                    <tr key={det.id}>
                                        <td>{det.cliente.nombre} {det.cliente.apellido}</td>
                                        <td>${det.tarifa}</td>
                                        <td>{det.porcentajeDescuentoGrupo}%</td>
                                        <td>${det.descuentoGrupo}</td>
                                        <td>{det.porcentajeDescuentoEspecial}%</td>
                                        <td>${det.descuentoEspecial}</td>
                                        <td>${det.descuentoExtra}</td>
                                        <td>${det.montoFinal}</td>
                                        <td>${det.montoIva}</td>
                                        <td>${det.montoTotal}</td>
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

export default ReservaDetalle;
