import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { Translate, translate, ValidatedField } from 'react-jhipster';
import { Row, Col, Alert } from 'reactstrap';

import { useAppSelector } from 'app/config/store';
import './home.scss';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);
  const [file, setFile] = useState<File | null>(null);
  const [uploading, setUploading] = useState<boolean>(false);
  const [success, setSuccess] = useState<boolean | null>(null);
  const [downloadLink, setDownloadLink] = useState<string | null>(null);
  const [message, setMessage] = useState('');

  const labels = ['bidPrjId', 'bidPrjName', 'bidSectionId', 'bidSection', 'bidder', 'bidPrice'];
  // 假设这个函数是从某处获取headslist数据
  const [headslist, setHeadslist] = useState([]);
  const [selections, setSelections] = useState({});

  // 当 headslist 更新时，初始化 selections 状态
  useEffect(() => {
    const initialSelections = {};
    labels.forEach(label => {
      initialSelections[label] = headslist[0];
    });
    setSelections(initialSelections);
  }, [headslist]);

  const fetchHeadData = () => {
    if (!file) return;
    setUploading(true);
    const formData = new FormData();
    formData.append('excel', file);
    fetch('/api/heads', {
      method: 'POST',
      body: formData,
    })
      .then(response => {
        if (response.ok) {
          return response.json();
        } else {
          // 处理错误情况
          throw new Error('File upload failed.');
        }
      })
      .then(data => {
        // 假设返回的数据是表头列表
        setHeadslist(data);
        setSuccess(true);
        setMessage('File uploaded and processed successfully.');
      })
      .catch(error => {
        console.error('Error:', error);
        setSuccess(false);
        setMessage(error.message || 'An error occurred while uploading the file.');
      })
      .finally(() => {
        setUploading(false);
      });
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const files = e.target.files;
    if (files && files.length > 0) {
      setFile(files[0]);
    }
  };

  const handleSubmit = () => {
    if (!file) return;
    setUploading(true);
    const formData = new FormData();
    const formDataAny = formData as any;
    formData.append('excel', file);

    // 将选中的键值对添加到表单数据中
    for (const [key, value] of Object.entries(selections)) {
      formData.append(key, String(value));
    }
    fetch('/api/upload', {
      method: 'POST',
      body: formData,
    })
      .then(response => {
        if (response.ok) {
          return response.json().then(data => {
            if (data && data.message) {
              setSuccess(true);
              setMessage(data.message);
            } else {
              setSuccess(false);
              setMessage('Upload successful, but no message received from the server.');
            }
          });
        } else {
          return response.json().then(errorData => {
            setSuccess(false);
            setMessage(errorData.message || 'Failed to upload and process the file.');
          });
        }
      })
      .catch(error => {
        console.error('Error uploading the file:', error);
        setSuccess(true); // 即使捕获到异常，仍将上传状态设置为成功
        setMessage(translate('uploadProcessingIssue'));
      })
      .finally(() => {
        setUploading(false);
      });
  };

  return (
    <Row>
      <Col md="3" className="pad">
        <span className="hipster rounded" />
      </Col>
      <Col md="9">
        <p className="lead">
          <Translate contentKey="home.helper">welcome to helper</Translate>
        </p>
        {account?.login ? (
          <div>
            <p style={{ color: 'red' }}>
              <Translate contentKey="home.importWarning">import warning!</Translate>
              <Link to="/bid-info" className="btn btn-primary ">
                &nbsp;
                <Translate contentKey="tenderhelperApp.bidInfo.home.title">Bid Infos</Translate>
              </Link>
            </p>
            <input type="file" title={translate('home.chooser')} onChange={handleFileChange} disabled={uploading} />
            <button onClick={fetchHeadData} disabled={uploading || !file}>
              <Translate contentKey="home.uploadbtn">Upload</Translate>
            </button>
            {uploading && <p>上传中...</p>}
            {success === true && (
              <div>
                <p style={{ color: 'green' }}>{message}</p>
                <ul>
                  {labels.map((label, index) => (
                    <li key={index}>
                      <span style={{ display: 'none' }}>{label}</span>
                      <Translate contentKey={`home.excelItem.${label}`}>{label}</Translate>----
                      <select onChange={e => setSelections({ ...selections, [label]: e.target.value })}>
                        {headslist.map((head, headIndex) => (
                          <option key={headIndex} value={head}>
                            {head}
                          </option>
                        ))}
                      </select>
                      <p style={{ height: 10 }}></p>
                    </li>
                  ))}
                  <p></p>
                </ul>
                <button onClick={handleSubmit}>
                  <Translate contentKey="home.uploadConfirmbtn">Upload Confirm</Translate>
                </button>
              </div>
            )}
            {success === false && <p style={{ color: 'red' }}>导入数据失败，请重试。</p>}
            <p>
              <p></p>
            </p>
            <p>
              <p></p>
            </p>
            <Alert color="success">
              <Translate contentKey="home.logged.message" interpolate={{ username: account.login }}>
                You are logged in as user {account.login}.
              </Translate>
            </Alert>
          </div>
        ) : (
          <div>
            <Alert color="warning">
              <Translate contentKey="global.messages.info.authenticated.prefix">If you want to </Translate>

              <Link to="/login" className="alert-link">
                <Translate contentKey="global.messages.info.authenticated.link"> sign in</Translate>
              </Link>
              <Translate contentKey="global.messages.info.authenticated.suffix">
                , you can try the default accounts:
                <br />- Administrator (login=&quot;admin&quot; and password=&quot;admin&quot;)
                <br />- User (login=&quot;user&quot; and password=&quot;user&quot;).
              </Translate>
            </Alert>
          </div>
        )}
      </Col>
    </Row>
  );
};
export default Home;
