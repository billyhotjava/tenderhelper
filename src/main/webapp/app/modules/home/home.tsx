import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { Translate, translate, ValidatedField } from 'react-jhipster';
import { Row, Col, Alert } from 'reactstrap';

import { useAppSelector } from 'app/config/store';
import './home.scss';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);
  const [file, setFile] = useState<File | null>(null);
  const [uploading, setUploading] = useState<boolean>(false);
  const [success, setSuccess] = useState<boolean | null>(null);
  const [downloadLink, setDownloadLink] = useState<string | null>(null);
  const [message, setMessage] = useState('');

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const files = e.target.files;
    if (files && files.length > 0) {
      setFile(files[0]);
    }
  };

  /** 
  const handleSubmit = () => {
    if (!file) return;

    setUploading(true);

    const formData = new FormData();
    formData.append('excel', file);
    try {
        const response = await fetch('/api/upload', {
            method: 'POST',
            body: formData,
        });

        if (response.ok) {
            const data = await response.json();

            if (data && data.message) {
                setSuccess(true);
                setMessage(data.message);
            } else {
                setSuccess(false);
                setMessage('Upload successful, but no message received from the server.');
            }
        } else {
            const errorData = await response.json();
            setSuccess(false);
            setMessage(errorData.message || 'Failed to upload and process the file.');
        }
    } catch (error) {
        console.error("Error uploading the file:", error);
        setSuccess(false);
        setMessage('An error occurred while uploading the file.');
    } finally {
        setUploading(false);
    }
  };**/

  const handleSubmit = () => {
    if (!file) return;

    setUploading(true);

    const formData = new FormData();
    formData.append('excel', file);

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
        setSuccess(false);
        setMessage('An error occurred while uploading the file.');
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
            <input type="file" title={translate('home.chooser')} onChange={handleFileChange} disabled={uploading} />
            <button onClick={handleSubmit} disabled={uploading || !file}>
              <Translate contentKey="home.uploadbtn">Upload</Translate>
            </button>
            {uploading && <p>上传中...</p>}
            {success === true && (
              <div>
                <p style={{ color: 'green' }}>{message}</p>
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
