import React from 'react';
import {User} from '../api/user.model';

export type OptionalUser = User | undefined

export const UserContext = React.createContext<OptionalUser>(undefined);
