import './Spinner.css';

const Spinner = ({ mensaje = "Cargando..." }) => {
    return (
        <div className="spinner-container">
            <div className="spinner" />
            <p>{mensaje}</p>
        </div>
    );
};

export default Spinner;
