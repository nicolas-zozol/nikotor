import React from 'react';

export function Login() {
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
      <div className="form-group form-check">
        <input
          type="checkbox"
          className="form-check-input"
          id="stay-connected"
        />
        <label className="stay-connected" htmlFor="stay-connected">
          Stay connected
        </label>
      </div>
      <button type="submit" className="btn btn-primary">
        Submit
      </button>
    </form>
  );
}
