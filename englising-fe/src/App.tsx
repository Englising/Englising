import '../src/index.css'
import './App.css'
import {
  BrowserRouter,
  Route,
  Routes,
} from 'react-router-dom'
import SelectMultiPage from './pages/SelectMultiPage'
import IndexPage from './pages/IndexPage'
import SelectSinglePage from './pages/SelectSinglePage'
import SettingMulti from './pages/SettingMultiPage'
import WaitingRoomPage from './pages/WaitingRoomPage'
import SinglePage from './pages/SinglePage'

function App() {

  return (
    <div>
      <BrowserRouter>
      <div className="App">
        <Routes>
          <Route path ="/" element= {<SelectMultiPage/>} />
            <Route path ="/index" element= {<IndexPage/>} />
            <Route path ="/selectMulti" element= {<SelectMultiPage/>} />
            <Route path ="/select" element= {<SelectSinglePage/>} />
            <Route path ="/settingMulti" element= {<SettingMulti/>}/>
            <Route path ="/waitingroom" element= {<WaitingRoomPage/>} />
            <Route path ="/singlepage" element= {<SinglePage/>} />
        </Routes>
      </div>
    </BrowserRouter>
    </div>
  )
}

export default App