import React, {FormEvent, useReducer} from 'react';
import {User} from "../api/user.model";
import {Action} from "../forms/Action";
import {login} from "../api/login-api";

type FullLoginForm = {
    email: string;
    password: string;
    stayConnected: boolean
}
type LoginForm = Partial<FullLoginForm>

type LoginProps = {
    updateLoggedUser: (user: User) => void;
}

export function Login({updateLoggedUser}: LoginProps) {

    const reducer = (state: LoginForm, action: Action): LoginForm => {
        switch (action.type) {
            case "email":
                return {...state, email: action.value};
            case "password":
                return {...state, password: action.value};
            case "stayConnected":
                return {...state, stayConnected: action.value === "on"}
            default:
                throw new Error();
        }
    };
    const [state, dispatch] = useReducer(reducer, {});

    const isFormValid = () => state.email && state.password && state.email.length > 0 && state.password.length > 0;

    return (
        <form onSubmit={(e) => handleLoginSubmit(e, state as FullLoginForm, updateLoggedUser)}>
            <div className="form-group">
                <label htmlFor="input-email">Email address</label>
                <input
                    type="email"
                    className="form-control"
                    id="input-email"
                    placeholder="Enter email"
                    onChange={event => dispatch({type: "email", value: event.target.value})}
                />
            </div>
            <div className="form-group">
                <label htmlFor="input-password">Password</label>
                <input
                    type="password"
                    className="form-control"
                    id="input-password"
                    placeholder="Password"
                    onChange={event => dispatch({type: "password", value: event.target.value})}
                />
            </div>
            <div className="form-group form-check">
                <input
                    type="checkbox"
                    className="form-check-input"
                    id="stay-connected"
                    onChange={event => dispatch({type: "stayConnected", value: event.target.value})}
                />
                <label className="stay-connected" htmlFor="stay-connected">
                    Stay connected
                </label>
            </div>
            <button type="submit" className="btn btn-primary" disabled={!isFormValid()}>
                Login
            </button>
        </form>
    );
}

function handleLoginSubmit(event: FormEvent, state: FullLoginForm, updateLoggedUser: (user: User) => void) {
    event.preventDefault();
    login(state.email, state.password).then(updateLoggedUser)

}