import axios from 'axios';
import {apiRoot} from './api';

const loginApi = apiRoot + 'login';

export function login(username: string, password: string) {
  axios.post(loginApi, {username, password}).then();
}
