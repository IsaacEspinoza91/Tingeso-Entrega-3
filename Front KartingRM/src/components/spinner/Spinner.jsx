import './Spinner.css';
import PropTypes from 'prop-types';

const Spinner = ({ mensaje = "Cargando..." }) => {
    return (
        <div className="spinner-container">
            <div className="spinner" />
            <p>{mensaje}</p>
        </div>
    );
};

Spinner.propTypes = {
    mensaje: PropTypes.string
};


export default Spinner;