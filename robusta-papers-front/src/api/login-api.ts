import axios from 'axios';
import {apiRoot} from './api';
import {User} from "./user.model";

const loginApi = apiRoot + 'login';
const registerApi = apiRoot + 'register';

export function login(email: string, password: string): Promise<User> {
    return axios.post(loginApi, {email, password})
}

export function registerNewUser(email: string, password: string,
                                lastname: string, firstname: string): Promise<boolean> {
    return axios.post(registerApi, {
        user: {
            email, lastname, firstname
        },
        password
    })
}
