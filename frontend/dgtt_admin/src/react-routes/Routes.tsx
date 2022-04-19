import { Routes, Route, Navigate } from 'react-router-dom';
import Login from '../modules/login/Index';
import LayoutApp from '../layouts/Layout';
import Home from '../modules/home/Index';
import AccountDetail from '../modules/account/detail/Index';
import PrivateRoute from './PrivateRoute';
import ChangePassword from '../modules/account/change-password/Index';

const IndexRoutes = () => {
    return (
        <>
            <Routes>
                <Route path='/login' element={<Login />} />
                <Route path="/" element={<PrivateRoute><LayoutApp /></PrivateRoute>}>
                    <Route path='' element={<PrivateRoute><Home /></PrivateRoute>} />

                    {/* account */}
                    <Route path='account/detail' element={<PrivateRoute><AccountDetail /></PrivateRoute>} />
                    <Route path='account/change-password' element={<PrivateRoute><ChangePassword /></PrivateRoute>} />

                    <Route path='*' element={<Navigate to="/" />} />
                </Route>
            </Routes>
        </>
    );
}

export default IndexRoutes;
