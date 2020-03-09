import React, {FormEvent, useReducer} from 'react';
import {Action} from "../forms/Action";
import {registerNewUser} from "../api/login-api";


type FullRegisterForm = {
    email: string;
    password: string;
    repeatPassword: string;
    validPassword: boolean;
    lastname: string;
    firstname: string;
}
type RegisterForm = Partial<FullRegisterForm>

type RegisterProps = {
    onRegister: () => void;
}


export function Register({onRegister}: RegisterProps) {
    const reducer = (state: RegisterForm, action: Action): RegisterForm => {
        switch (action.type) {
            case "email":
                return {...state, email: action.value};
            case "password":
                return {...state, password: action.value, validPassword: isValidPassword(state)};
            case "repeat-password":
                return {...state, repeatPassword: action.value}
            case "lastname":
                return {...state, lastname: action.value};
            case "firstname":
                return {...state, firstname: action.value};
            default:
                throw new Error();
        }
    };
    const [state, dispatch] = useReducer(reducer, {});


    const isFormValid = () => state.email && state.email.length > 0 && isValidPassword(state);

    return (
        <form onSubmit={(e) => handleRegisterSubmit(e, state as FullRegisterForm, onRegister)}>
            <div className="form-group">
                <label htmlFor="input-email">Email address</label>
                <input
                    type="email"
                    className="form-control"
                    id="input-email"
                    placeholder="Enter email"
                    name="email"
                    onChange={event => dispatch({type: "email", value: event.target.value})}
                />
                <small id="emailHelp" className="form-text text-muted">
                    We'll never share your email with anyone else.
                </small>
            </div>
            <div className="form-group">
                <label htmlFor="input-password">Password</label>
                <input
                    type="password"
                    className="form-control"
                    id="input-password"
                    placeholder="Password"
                    name="password"
                    onChange={event => dispatch({type: "password", value: event.target.value})}
                />
            </div>
            <div className="form-group">
                <label htmlFor="input-password-check">Repeat Password</label>
                <input
                    type="password"
                    className="form-control"
                    id="input-password-check"
                    placeholder="Repeat Password"
                    name="repeat-password"
                    onChange={event => dispatch({type: "repeat-password", value: event.target.value})}
                />
            </div>

            <div className="form-group">
                <label htmlFor="input-last-name">Last name</label>
                <input
                    type="text"
                    className="form-control"
                    id="input-lastname"
                    placeholder="Enter last name"
                    name="lastname"
                    onChange={event => dispatch({type: "lastname", value: event.target.value})}
                />
            </div>

            <div className="form-group">
                <label htmlFor="input-first-name">First name</label>
                <input
                    type="text"
                    className="form-control"
                    id="input-firstname"
                    placeholder="Enter first name"
                    name="firstname"
                    onChange={event => dispatch({type: "firstname", value: event.target.value})}
                />
            </div>

            <button type="submit" className="btn btn-primary" disabled={!isFormValid()}>
                Register
            </button>
        </form>
    );
}

function handleRegisterSubmit(event: FormEvent, state: FullRegisterForm, onRegister: () => void) {
    event.preventDefault();
    registerNewUser(state.email, state.password, state.lastname, state.firstname).then(onRegister)

}

function isValidPassword(state: RegisterForm): boolean {
    return state.password !== undefined
        && state.password === state.repeatPassword
        && state.password.length > 0;
}
