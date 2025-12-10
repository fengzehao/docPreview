<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, user-scalable=yes, initial-scale=1.0">
    <title>PDF预览</title>
    <link href="/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
    <style>
        body {
            background-color: #f5f5f5;
        }

        .image {
            width: 100%;
            margin-bottom: 8px;
            border: 1px solid #e0e0e0;
            border-radius: 4px;
            box-shadow: 0 1px 2px rgba(0, 0, 0, .1);
        }
    </style>
</head>
<body>
<nav class="navbar bg-white shadow-sm px-4">
    <span class="navbar-brand">PDF预览</span>
    <span class="text-muted small" id="total"></span>
</nav>
<div class="position-fixed top-50 start-50 translate-middle" id="loading">
    <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">加载中…</span>
    </div>
</div>
<div class="container-fluid pt-2" id="container"></div>
<script>
    (async function () {
        try {
            const res = await fetch('/api/convert?url=${url}', {method: 'POST'});
            const data = await res.json();
            if (!data.flag) throw new Error('转换失败');
            let loaded = 0;
            const md5 = data.msg.md5;
            const total = data.msg.total;
            document.getElementById('total').textContent = total + '页';
            const container = document.getElementById('container');
            for (let i = 0; i < total; i++) {
                const img = document.createElement('img');
                img.src = '/' + md5 + '/' + i + '.jpg';
                img.className = 'image';
                img.alt = 'image';
                img.onload = function() {
                    if (++loaded === total) {
                        document.getElementById('loading').style.display = 'none';
                    }
                }
                container.appendChild(img);
            }
        } catch (e) {
            document.getElementById('loading').innerHTML = '<div class="text-danger">转换失败，请稍后重试</div>';
        }
    })();
</script>
</body>
</html>
