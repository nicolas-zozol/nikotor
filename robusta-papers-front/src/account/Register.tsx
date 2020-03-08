import React from 'react';

export function Register() {
  return (
    <form>
      <div className="form-group">
        <label htmlFor="input-email">Email address</label>
        <input
          type="email"
          className="form-control"
          id="input-email"
          placeholder="Enter email"
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
        />
      </div>
      <div className="form-group">
        <label htmlFor="input-password-check">Repeat Password</label>
        <input
          type="password"
          className="form-control"
          id="input-password-check"
          placeholder="Repeat Password"
        />
      </div>

      <div className="form-group">
        <label htmlFor="input-last-name">Last name</label>
        <input
          type="text"
          className="form-control"
          id="input-lastname"
          placeholder="Enter last name"
        />
      </div>

      <div className="form-group">
        <label htmlFor="input-first-name">First name</label>
        <input
          type="text"
          className="form-control"
          id="input-firstname"
          placeholder="Enter first name"
        />
      </div>

      <button type="submit" className="btn btn-primary">
        Submit
      </button>
    </form>
  );
}
