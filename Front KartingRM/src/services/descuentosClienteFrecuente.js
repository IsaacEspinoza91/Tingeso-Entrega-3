import httpClient from '../http-common';

const BASE_URL = '/api/cliente-service/descuento-frecuente';

export const getAllFrequentCustomerDiscounts = async () => {
    try {
        const response = await httpClient.get(`${BASE_URL}/`);
        return response.data;
    } catch (error) {
        console.error("Error al obtener descuentos de clientes frecuentes", error);
        throw error;
    }
};

export const getFrequentCustomerDiscountById = async (id) => {
    try {
        const response = await httpClient.get(`${BASE_URL}/${id}`);
        return response.data;
    } catch (error) {
        console.error(`Error al obtener descuento con ID ${id}`, error);
        throw error;
    }
};

export const createFrequentCustomerDiscount = async (data) => {
    try {
        const response = await httpClient.post(`${BASE_URL}/`, data);
        return response.data;
    } catch (error) {
        console.error('Error al crear descuento para cliente frecuente:', error);
        throw error;
    }
};

export const updateFrequentCustomerDiscount = async (id, data) => {
    try {
        const response = await httpClient.put(`${BASE_URL}/${id}`, data);
        return response.data;
    } catch (error) {
        console.error(`Error al actualizar descuento con ID ${id}:`, error);
        throw error;
    }
};

export const deleteFrequentCustomerDiscount = async (id) => {
    try {
        await httpClient.delete(`${BASE_URL}/${id}`);
        return id;
    } catch (error) {
        console.error(`Error al eliminar descuento con ID ${id}:`, error);
        throw error;
    }
};

export default {
    getAllFrequentCustomerDiscounts,
    getFrequentCustomerDiscountById,
    createFrequentCustomerDiscount,
    updateFrequentCustomerDiscount,
    deleteFrequentCustomerDiscount
};