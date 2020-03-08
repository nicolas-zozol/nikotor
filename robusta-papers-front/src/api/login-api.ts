import axios from 'axios';
import {apiRoot} from './api';
import {User} from "./user.model";

const loginApi = apiRoot + 'login';

export function login(username: string, password: string): Promise<User> {
    return axios.post(loginApi, {username, password})
}
