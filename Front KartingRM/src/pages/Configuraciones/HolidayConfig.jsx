import { useState, useEffect } from 'react';
import {
    getAllDiasFeriados,
    getDiasFeriadosByAnio,
    createDiaFeriado,
    updateDiaFeriado,
    deleteDiaFeriado
} from '../../services/diasFeriadosEspeciales';
import './HolidayConfig.css';

const HolidayConfig = ({ onBack }) => {
    const [diasFeriados, setDiasFeriados] = useState([]);
    const [anioBusqueda, setAnioBusqueda] = useState('');
    const [mostrarFormulario, setMostrarFormulario] = useState(false);
    const [formData, setFormData] = useState({
        nombre: '',
        fecha: ''
    });
    const [editandoId, setEditandoId] = useState(null);
    const [cargando, setCargando] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        cargarTodosLosDias();
    }, []);

    const cargarTodosLosDias = async () => {
        setCargando(true);
        setError(null);
        try {
            const datos = await getAllDiasFeriados();
            setDiasFeriados(datos);
            setAnioBusqueda('');
        } catch (err) {
            setError('Error al cargar días feriados');
            console.error(err);
        } finally {
            setCargando(false);
        }
    };

    const buscarPorAnio = async () => {
        if (!anioBusqueda) {
            cargarTodosLosDias();
            return;
        }

        setCargando(true);
        setError(null);
        try {
            const datos = await getDiasFeriadosByAnio(anioBusqueda);
            setDiasFeriados(datos);
        } catch (err) {
            setError('Error al buscar días feriados');
            console.error(err);
        } finally {
            setCargando(false);
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
        setCargando(true);
        setError(null);

        try {
            if (editandoId) {
                await updateDiaFeriado(editandoId, formData);
            } else {
                await createDiaFeriado(formData);
            }
            setMostrarFormulario(false);
            setFormData({ nombre: '', fecha: '' });
            setEditandoId(null);
            if (anioBusqueda && formData.fecha.includes(anioBusqueda)) {
                buscarPorAnio();
            } else {
                cargarTodosLosDias();
            }
        } catch (err) {
            setError(editandoId ? 'Error al actualizar día' : 'Error al crear día');
            console.error(err);
        } finally {
            setCargando(false);
        }
    };

    const handleEditar = (dia) => {
        setFormData({
            nombre: dia.nombre,
            fecha: dia.fecha.split('T')[0] // Asegura el formato YYYY-MM-DD
        });
        setEditandoId(dia.id);
        setMostrarFormulario(true);
    };

    const handleEliminar = async (id) => {
        if (!window.confirm('¿Estás seguro de eliminar este día feriado?')) return;

        setCargando(true);
        setError(null);
        try {
            await deleteDiaFeriado(id);
            if (anioBusqueda) {
                buscarPorAnio();
            } else {
                cargarTodosLosDias();
            }
        } catch (err) {
            setError('Error al eliminar día');
            console.error(err);
        } finally {
            setCargando(false);
        }
    };

    const formatearFecha = (fechaISO) => {
        const opciones = { year: 'numeric', month: 'long', day: 'numeric' };
        return new Date(fechaISO + "T00:00:00").toLocaleDateString('es-ES', opciones);
    };

    return (
        <div className="holiday-config-container">
            <button className="back-button" onClick={onBack}>
                ←
            </button>

            <div className="holiday-header">
                <h2>Definición Días Feriados y especiales</h2>

                <div className="search-controls">
                    <div className="search-input-group">
                        <label htmlFor="anio">Buscar por año:</label>
                        <input
                            type="number"
                            id="anio"
                            value={anioBusqueda}
                            onChange={(e) => setAnioBusqueda(e.target.value)}
                            min="2000"
                            max="2100"
                            placeholder="Ej: 2023"
                            disabled={cargando}
                        />
                        <button
                            className="search-button"
                            onClick={buscarPorAnio}
                            disabled={cargando}
                        >
                            Buscar
                        </button>
                        <button
                            className="show-all-button"
                            onClick={cargarTodosLosDias}
                            disabled={cargando || !anioBusqueda}
                        >
                            Mostrar Todos
                        </button>


                        <button
                            className="add-button"
                            onClick={() => {
                                setMostrarFormulario(true);
                                setEditandoId(null);
                                setFormData({ nombre: '', fecha: '' });
                            }}
                            disabled={cargando}
                        >
                            + Añadir Día
                        </button>
                    </div>
                </div>
            </div>

            {error && <div className="error-message">{error}</div>}

            {mostrarFormulario && (
                <div className="form-modal">
                    <div className="form-container">
                        <h3>{editandoId ? 'Editar Día Feriado' : 'Nuevo Día Feriado'}</h3>
                        <form onSubmit={handleSubmit}>
                            <div className="form-group">
                                <label>Nombre:</label>
                                <input
                                    type="text"
                                    name="nombre"
                                    value={formData.nombre}
                                    onChange={handleInputChange}
                                    required
                                    disabled={cargando}
                                />
                            </div>
                            <div className="form-group">
                                <label>Fecha:</label>
                                <input
                                    type="date"
                                    name="fecha"
                                    value={formData.fecha}
                                    onChange={handleInputChange}
                                    required
                                    disabled={cargando}
                                />
                            </div>
                            <div className="form-actions">
                                <button type="submit" disabled={cargando}>
                                    {cargando ? 'Guardando...' : 'Guardar'}
                                </button>
                                <button
                                    type="button"
                                    onClick={() => setMostrarFormulario(false)}
                                    disabled={cargando}
                                >
                                    Cancelar
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}

            <div className="dias-list-container">
                <div className="dias-list-header">
                    <h3>
                        {anioBusqueda
                            ? `Días Feriados para ${anioBusqueda}`
                            : 'Todos los Días Feriados y Especiales'}
                    </h3>
                    <span className="total-count">{diasFeriados.length} registros</span>
                </div>

                {cargando && !mostrarFormulario ? (
                    <div className="loading-message">Cargando días feriados...</div>
                ) : diasFeriados.length === 0 ? (
                    <div className="empty-message">
                        {anioBusqueda
                            ? `No hay días feriados registrados para el año ${anioBusqueda}`
                            : 'No hay días feriados registrados'}
                    </div>
                ) : (
                    <div className="table-container">
                        <table>
                            <thead>
                                <tr>
                                    <th>Nombre</th>
                                    <th>Fecha</th>
                                    <th>Acciones</th>
                                </tr>
                            </thead>
                            <tbody>
                                {diasFeriados.map((dia) => (
                                    <tr key={dia.id}>
                                        <td>{dia.nombre}</td>
                                        <td>{formatearFecha(dia.fecha)}</td>
                                        <td className="actions-cell">
                                            <button
                                                className="edit-button"
                                                onClick={() => handleEditar(dia)}
                                                disabled={cargando}
                                            >
                                                Editar
                                            </button>
                                            <button
                                                className="delete-button"
                                                onClick={() => handleEliminar(dia.id)}
                                                disabled={cargando}
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

export default HolidayConfig;