import React from 'react';
import styled from 'styled-components';
import {faUser} from '@fortawesome/free-solid-svg-icons';
import {Link} from 'react-router-dom';
import {SpaceIcon} from '../design/SpaceIcon';

const Nav = styled.ul`
  display: flex;
  list-style: none;
  justify-content: space-around;
  align-items: center;
  padding: 0;
  margin: 20px;
`;

export function TopBar() {
  return (
    <Nav>
      <li>
        <Link to="/">Home</Link>
      </li>
      <li>
        <Link to="/login">
          <SpaceIcon icon={faUser}>Login</SpaceIcon>
        </Link>
      </li>
      <li>
        <Link to="/register">Register</Link>
      </li>
    </Nav>
  );
}
