import axios from 'axios';
import {apiRoot} from './api';
import {User} from "./user.model";

const loginApi = apiRoot + 'login';
const registerApi = apiRoot + 'login';

export function login(username: string, password: string): Promise<User> {
    return axios.post(loginApi, {username, password})
}

export function registerNewUser(username: string, password: string,
                         lastname: string, firstname: string): Promise<boolean> {
    return axios.post(registerApi, {username, password})
}
