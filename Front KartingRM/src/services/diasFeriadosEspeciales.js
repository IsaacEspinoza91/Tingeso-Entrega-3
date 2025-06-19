import httpClient from '../http-common';

const BASE_URL = '/api/dias-especiales-service/dias-feriados';


export const getAllDiasFeriados = async () => {
    try {
        const response = await httpClient.get(`${BASE_URL}/`);
        return response.data;
    } catch (error) {
        console.error("Error al obtener dias feriados y especiales", error);
        throw error;
    }
};

export const getDiasFeriadosByAnio = async (anio) => {
    try {
        const response = await httpClient.get(`${BASE_URL}/anio/${anio}`);
        return response.data;
    } catch (error) {
        console.error("Error al obtener dias feriados y especiales segun anio", error);
        throw error;
    }
};

export const createDiaFeriado = async (data) => {
    try {
        const response = await httpClient.post(`${BASE_URL}/`, data);
        return response.data;
    } catch (error) {
        console.error('Error al crear dia feriado o especial:', error);
        throw error;
    }
};

export const updateDiaFeriado = async (id, data) => {
    try {
        const response = await httpClient.put(`${BASE_URL}/${id}`, data);
        return response.data;
    } catch (error) {
        console.error(`Error al actualizar el dia con ID ${id}:`, error);
        throw error;
    }
};

export const deleteDiaFeriado = async (id) => {
    try {
        await httpClient.delete(`${BASE_URL}/${id}`);
        return id;
    } catch (error) {
        console.error(`Error al eliminar dia con ID ${id}:`, error);
        throw error;
    }
};

export default {
    getAllDiasFeriados,
    getDiasFeriadosByAnio,
    createDiaFeriado,
    updateDiaFeriado,
    deleteDiaFeriado
};